package id.bni46.microatmsdk.devices.mpos

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.mf.mpos.pub.CommEnum
import com.mf.mpos.pub.Controler
import com.mf.mpos.pub.param.InputPinParam
import com.mf.mpos.pub.param.ReadCardParam
import com.mf.mpos.util.Misc
import id.bni46.microatmsdk.BuildConfig
import id.bni46.microatmsdk.constan.ByteUtils
import id.bni46.microatmsdk.constan.BytesUtil
import id.bni46.microatmsdk.data.EmvResult
import id.bni46.microatmsdk.data.MethodResult
import id.bni46.microatmsdk.data.ProgressMessage
import id.bni46.microatmsdk.extension.calcKvc
import id.bni46.microatmsdk.extension.keyIndex
import id.bni46.microatmsdk.extension.toHex
import id.bni46.microatmsdk.utilities.MessageProgressEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface Mp63 {
    private val TAG: String
        get() = "Mp63"
    val activity: Activity?
    suspend fun connect(device: BluetoothDevice): Boolean {
        return withContext(Dispatchers.Main) {
            delay(500)
            MessageProgressEvent.sentData(
                ProgressMessage(
                    message = "Connecting ${device.name}",
                    hasProgress = true
                )
            )
            Controler.Init(activity, CommEnum.CONNECTMODE.BLUETOOTH, 0)
            val con = Controler.connectPos(device.address)
            if (!con.bConnected) {
                MessageProgressEvent.sentData(
                    ProgressMessage(
                        message = con.errmsg,
                        hasProgress = false
                    )
                )
            }
            return@withContext con.bConnected
        }
    }

    suspend fun startEmv(): String? {
        return withContext(Dispatchers.IO) {
            val param = ReadCardParam()
            param.amount = 1000000
            param.transType = CommEnum.TRANSTYPE.FUNC_SALE
            param.isPinInput = 0x01
            param.setRequireReturnCardNo(0x01)
            param.emvTransactionType = 0x00
            param.transName = "consume"
            Controler.SetEmvParamTlv("9F1A0203605F2A0203609F3303E0F8C8")
            param.onSteplistener = object : ReadCardParam.onStepListener {
                override fun onStep(p0: Byte) {
                    when (p0) {
                        1.toByte() -> {
                            MessageProgressEvent.sentDataOnMainThread(ProgressMessage(message = "Please Insert Your Card"))
                        }

                        2.toByte() -> {
                            MessageProgressEvent.sentDataOnMainThread(ProgressMessage(message = "Reading card"))
                        }

                        3.toByte() -> {
                            MessageProgressEvent.sentDataOnMainThread(ProgressMessage(message = "Enter the password"))
                        }

                        4.toByte() -> {
                            MessageProgressEvent.sentDataOnMainThread(ProgressMessage(message = "Enter the amount"))
                        }

                        else -> {
                            MessageProgressEvent.sentDataOnMainThread(ProgressMessage(message = "STEP CODE $p0"))
                        }
                    }
                }

                override fun onPwdLength(p0: Int) {
                    Log.d(TAG, "onPwdLength: $p0")
                }

            }
            val readCard = Controler.ReadCard(param)
            return@withContext EmvResult(
                communication = readCard.commResult.name,
                cardType = "${readCard.cardType}",
                pan = readCard.pan,
                t2d = readCard.track2,
                icCardData = if (readCard.cardType == 2) readCard.icData else "",
                expDate = readCard.expData
            ).toJson()
        }
    }


    fun injectMasterKey(): String? {
        val calcKvc = BuildConfig.MASTER_KEY.calcKvc(true)
        val keyBuf = BytesUtil.hexString2ByteArray(BuildConfig.MASTER_KEY)
        val kekD1 = BytesUtil.subBytes(keyBuf, 0, 8)
        val kekD2 = BytesUtil.subBytes(keyBuf, 8, 8)
        val kvcBuf = BytesUtil.hexString2ByteArray(calcKvc)
        val result = Controler.LoadMainKey(
            CommEnum.MAINKEYENCRYPT.PLAINTEXT,
            BuildConfig.MASTER_KEY.keyIndex(),
            CommEnum.MAINKEYTYPE.DOUBLE,
            kekD1, kekD2, kvcBuf
        )
        return MethodResult(message = result.commResult.name, data = result.loadResult).toJson()
    }

    fun injectWorkKey(): String? {
        val key: String =
            BuildConfig.PIN_KEY + BuildConfig.PIN_KEY.calcKvc(false) + BuildConfig.MAC_KEY + BuildConfig.MAC_KEY.calcKvc(
                false
            ) + BuildConfig.TDK_KEY + BuildConfig.TDK_KEY.calcKvc(false)
        val keyArrays = Misc.asc2hex(key)
        val result = Controler.LoadWorkKey(
            CommEnum.KEYINDEX.INDEX0,
            CommEnum.WORKKEYTYPE.DOUBLEMAG,
            keyArrays,
            keyArrays.size
        )
        return MethodResult(message = result.commResult.name, data = result.loadResult).toJson()
    }

    fun inputtingPin(): String? {
        val param = InputPinParam(
            6,
            0,
            60, "5371762590003419"
        )
        param.line1Tip = "line1Tip"
        param.line2Tip = "line2Tip"
        param.line3Tip = "line3Tip"
        param.line4Tip = "line4Tip"
        return try {
            val ret = Controler.InputPin(param)
            if (ret.commResult == CommEnum.COMMRET.NOERROR) {
                val res = BytesUtil.bytes2Hex(ret.pinBlock)
                MethodResult(message = ret.commResult.name, data = res.toHex()).toJson()
            } else {
                MethodResult(message = ret.commResult.name, data = null).toJson()
            }
        } catch (e: NumberFormatException) {
            MethodResult(message = "NumberFormatException ${e.message}", data = null).toJson()

        } catch (e: NullPointerException) {
            MethodResult(message = "NullPointerException ${e.message}", data = null).toJson()

        } catch (e: Exception) {
            MethodResult(message = "Exception ${e.message}", data = null).toJson()

        }
    }

    fun encryption(data: String, context: Context): String? {
//        if (data.length % 8 != 0) {
//            Toast.makeText(context, "length must be %8", Toast.LENGTH_SHORT).show()
//            return "length must be %8"
//        }

        val input = if (data.length % 8 == 0) {
            data
        } else {
            data.padEnd(data.length + (8 - data.length % 8), '0')
        }
        println("test data input : $input")

        val dataValueBuf = ByteUtils.stringToHex(input)
//        val dataLengthBuf = ByteArray(2)
//
//        dataLengthBuf[0] = Hex2BCD(dataValueBuf.size / 100 and 0xff)
//        dataLengthBuf[1] = Hex2BCD(dataValueBuf.size % 100 and 0xff)
        println("test data dataValueBuf Enc : $dataValueBuf")
        val result = Controler.encryptOrDecrypt(
            CommEnum.EncryptsettingEnum.ENCRYPT,
            CommEnum.AlignmentEnum.ECB,
            CommEnum.EncryptEnum.DATAKEY,
            CommEnum.KEYINDEX.INDEX0,
            ByteUtils.hexString2ByteArray(dataValueBuf)
        )
        val ret = ByteUtils.byteArray2HexString(result.checkvalue)
        return if (result.commResult == CommEnum.COMMRET.NOERROR) {
            MethodResult(message = result.commResult.name, data = ret.substring(4)).toJson()
        } else {
            MethodResult(message = result.commResult.name, data = null).toJson()
        }

    }

    fun decryption(data: String, context: Context): String? {
//        if (data.length % 8 != 0) {
//            Toast.makeText(context, "length must be %8", Toast.LENGTH_SHORT).show()
//            return "length must be %8"
//        }

        val dataValueBuf = ByteUtils.hexStr2Bytes(data)
//        val dataLengthBuf = ByteArray(2)
//
//        dataLengthBuf[0] = Hex2BCD(dataValueBuf.size / 100 and 0xff)
//        dataLengthBuf[1] = Hex2BCD(dataValueBuf.size % 100 and 0xff)

        val result = Controler.encryptOrDecrypt(
            CommEnum.EncryptsettingEnum.DECRYPT,
            CommEnum.AlignmentEnum.ECB,
            CommEnum.EncryptEnum.DATAKEY,
            CommEnum.KEYINDEX.INDEX0,
            dataValueBuf
        )
        val ret = String(result.checkvalue)
        return if (result.commResult == CommEnum.COMMRET.NOERROR) {
            MethodResult(
                message = result.commResult.name,
                data = removeTextAfterLastCurlyBrace(ret.substring(2))
            ).toJson()
        } else {
            MethodResult(message = result.commResult.name, data = null).toJson()
        }
    }

    private fun Hex2BCD(a: Int): Byte {
        val tl: Int
        val th: Int = a / 10
        tl = a - th * 10
        val ret = (th shl 4) + tl
        return (ret and 0xff).toByte()
    }

    private fun removeTextAfterLastCurlyBrace(input: String): String {
        val lastCurlyBraceIndex = input.lastIndexOf('}')

        return if (lastCurlyBraceIndex >= 0) {
            input.substring(0, lastCurlyBraceIndex + 1)
        } else {
            input
        }
    }
}
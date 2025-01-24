import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.plugin.annotation.CapacitorMethod;
import com.getcapacitor.plugin.annotation.CapacitorReturnType;

public class NFCWriter {

    private NfcAdapter nfcAdapter;
    private NdefMessage messageToWrite;
    public Runnable onWriteSuccess;
    public Runnable onError;

    public void startWriting(NdefMessage message) {
        this.messageToWrite = message;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(event -> {
                if (messageToWrite != null) {
                    event.setNdefMessage(messageToWrite);
                    onWriteSuccess.run();
                }
            }, this);
        }
    }
}
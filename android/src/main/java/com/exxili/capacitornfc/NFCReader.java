import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.plugin.annotation.CapacitorMethod;
import com.getcapacitor.plugin.annotation.CapacitorReturnType;
import java.util.ArrayList;
import java.util.List;

public class NFCReader {

    private NfcAdapter nfcAdapter;
    public Runnable onNDEFMessageReceived;
    public Runnable onError;

    public void startScanning() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(event -> {
                List<NdefMessage> ndefMessages = new ArrayList<>();
                for (NdefRecord record : event.getRecords()) {
                    ndefMessages.add(new NdefMessage(new NdefRecord[] { record }));
                }
                onNDEFMessageReceived.run();
            }, this);
        }
    }
}
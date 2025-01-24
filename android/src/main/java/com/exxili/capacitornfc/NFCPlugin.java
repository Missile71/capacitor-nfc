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

@CapacitorPlugin(name = "NFCPlugin")
public class NFCPlugin extends BridgeActivity {

    private NFCReader reader = new NFCReader();
    private NFCWriter writer = new NFCWriter();

    @CapacitorMethod(
        returnType = CapacitorReturnType.PROMISE
    )
    public void startScan(PluginCall call) {
        reader.onNDEFMessageReceived = messages -> {
            List<NdefRecord> ndefRecords = new ArrayList<>();
            for (NdefMessage message : messages) {
                for (NdefRecord record : message.getRecords()) {
                    String recordType = new String(record.getType());
                    String payload = new String(record.getPayload());
                    ndefRecords.add(record);
                }
            }
            call.resolve();
        };

        reader.onError = error -> {
            call.reject(error.getMessage());
        };

        reader.startScanning();
    }

    @CapacitorMethod(
        returnType = CapacitorReturnType.PROMISE
    )
    public void writeNDEF(PluginCall call) {
        List<NdefRecord> ndefRecords = new ArrayList<>();
        List<PluginCall> recordsData = call.getArray("records");
        for (PluginCall recordData : recordsData) {
            String type = recordData.getString("type");
            String payload = recordData.getString("payload");
            if (type != null && payload != null) {
                ndefRecords.add(NdefRecord.createMime(type, payload.getBytes()));
            }
        }

        NdefMessage ndefMessage = new NdefMessage(ndefRecords.toArray(new NdefRecord[0]));

        writer.onWriteSuccess = () -> {
            call.resolve();
        };

        writer.onError = error -> {
            call.reject(error.getMessage());
        };

        writer.startWriting(ndefMessage);
    }
}
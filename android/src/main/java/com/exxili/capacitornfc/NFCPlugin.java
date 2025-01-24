import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.plugin.annotation.Permission;
import com.getcapacitor.plugin.annotation.PermissionRequestCode;
import com.getcapacitor.plugin.annotation.CapacitorData;
import com.getcapacitor.plugin.annotation.CapacitorReturnType;
import com.getcapacitor.plugin.annotation.NfcPlugin;

@CapacitorPlugin(
    name = "NFCPlugin",
    permissions = {
        @Permission(alias = "nfc", strings = { "android.permission.NFC" })
    }
)
public class NFCPlugin extends BridgeActivity implements NfcAdapter.CreateNdefMessageCallback {
    private NfcAdapter nfcAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @CapacitorMethod(
        returnType = CapacitorReturnType.PROMISE
    )
    public void startScan(PluginCall call) {
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            call.resolve();
        } else {
            call.reject("NFC not supported on this device");
        }
    }

    @CapacitorMethod(
        returnType = CapacitorReturnType.PROMISE
    )
    public void writeNDEF(PluginCall call) {
        try {
            PluginData recordsData = call.getData();
            List<NdefRecord> ndefRecords = new ArrayList<>();
            for (PluginData recordData : recordsData) {
                String type = recordData.getString("type");
                String payload = recordData.getString("payload");
                if (type != null && payload != null) {
                    ndefRecords.add(new NdefRecord(NdefRecord.TNF_WELL_KNOWN, type.getBytes(), new byte[0], payload.getBytes()));
                }
            }
            NdefMessage ndefMessage = new NdefMessage(ndefRecords.toArray(new NdefRecord[0]));
            this.setNdefMessage(ndefMessage);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to write NDEF message", e);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // Implement NDEF message creation logic here
        // This is a placeholder implementation
        String text = "Hello, World!";
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", text.getBytes());
        return new NdefMessage(new NdefRecord[] { ndefRecord });
    }
}

package com.example.myandroid.nfc;

import java.io.IOException;
import java.util.Arrays;

import com.example.myandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NfcCardReader extends Activity {

	private final String TAG = NfcCardReader.class.getSimpleName();

	private TextView mTextView;

	// AID for our loyalty card service.
	private static final String SAMPLE_LOYALTY_CARD_AID = "F222222222";
	// ISO-DEP command HEADER for selecting an AID.
	// Format: [Class | Instruction | Parameter 1 | Parameter 2]
	private static final String SELECT_APDU_HEADER = "00A40400";
	// "OK" status word sent in response to SELECT AID command (0x9000)
	private static final byte[] SELECT_OK_SW = { (byte) 0x90, (byte) 0x00 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_card_reader);
		initViews();
	}

	private void initViews() {
		mTextView = (TextView) findViewById(R.id.textView1);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		processIntent(getIntent());
	}

	private void processIntent(Intent intent) {
		// 取出封装在intent中的TAG
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		for (String tech : tagFromIntent.getTechList()) {
			System.out.println(tech);
		}
		System.out.println(tagFromIntent.toString());
		boolean auth = false;
		// 读取TAG
		IsoDep isoDep = IsoDep.get(tagFromIntent);
		if (isoDep != null) {
			try {
				// Connect to the remote NFC device
				isoDep.connect();
				// Build SELECT AID command for our loyalty card service.
				// This command tells the remote device which service we wish to
				// communicate with.
				Log.i(TAG, "Requesting remote AID: " + SAMPLE_LOYALTY_CARD_AID);
				byte[] command = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
				// Send command to remote device
				Log.i(TAG, "Sending: " + ByteArrayToHexString(command));
				byte[] result = isoDep.transceive(command);
				// If AID is successfully selected, 0x9000 is returned as the
				// status word (last 2
				// bytes of the result) by convention. Everything before the
				// status word is
				// optional payload, which is used here to hold the account
				// number.
				int resultLength = result.length;
				byte[] statusWord = { result[resultLength - 2],
						result[resultLength - 1] };
				byte[] payload = Arrays.copyOf(result, resultLength - 2);
				if (Arrays.equals(SELECT_OK_SW, statusWord)) {
					// The remote NFC device will immediately respond with its
					// stored account number
					String accountNumber = new String(payload, "UTF-8");
					Log.i(TAG, "Received: " + accountNumber);
					// Inform CardReaderFragment of received account number
					// mAccountCallback.get().onAccountReceived(accountNumber);
				}
			} catch (IOException e) {
				Log.e(TAG, "Error communicating with card: " + e.toString());
			}
		}
	}

	// 字符序列转换为16进制字符串
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("0x");
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	/**
	 * Build APDU for SELECT AID command. This command indicates which service a
	 * reader is interested in communicating with. See ISO 7816-4.
	 * 
	 * @param aid
	 *            Application ID (AID) to select
	 * @return APDU for SELECT AID command
	 */
	public static byte[] BuildSelectApdu(String aid) {
		// Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH |
		// DATA]
		return HexStringToByteArray(SELECT_APDU_HEADER
				+ String.format("%02X", aid.length() / 2) + aid);
	}

	/**
	 * Utility class to convert a byte array to a hexadecimal string.
	 * 
	 * @param bytes
	 *            Bytes to convert
	 * @return String, containing hexadecimal representation.
	 */
	public static String ByteArrayToHexString(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Utility class to convert a hexadecimal string to a byte string.
	 * 
	 * <p>
	 * Behavior with input strings containing non-hexadecimal characters is
	 * undefined.
	 * 
	 * @param s
	 *            String containing hexadecimal characters to convert
	 * @return Byte array generated from input
	 */
	public static byte[] HexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}

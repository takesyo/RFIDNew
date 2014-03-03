package rfid;

class Main{
	public static void main ( String[] args ) {
		RFIDAPI  rfidapi= new RFIDAPI();
		rfidapi.ConnectRFID();
		rfidapi.ReedRFIDsMemory();
		rfidapi.WriteRFIDsMemory();
		rfidapi.DisConnectRFID();
		
	}
}
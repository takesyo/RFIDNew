package rfid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class RFIDAPI {
	public native int ConnectRFIDonUSB ( int iType, int iPort, long ISpeed );  //USB�ڑ��pJNI�֐�
	public native int ConnectRFIDonLAN ( int iType, String IPAddress, int iPort ); //LAN�ڑ��pJNI�֐�
	public native int DisconnectRFID ( int iCommNo ); //�ڑ������pJNI�֐�
	public native int ReadRFIDsMem ( int iCommNo, int mb, int sa, int TMDLength, char[] MemoryData ); //�������Ǎ��pJNI�֐�
	public native int WriteRFIDsMem ( int iCommNo, int mb, int sa, int TMDLength, char[] MemoryData ); //�����������pJNI�֐�
	
	private int Result = 1; //��������p�ϐ� 	��{�I��0�ɂȂ��Ăق����z
	private int MB = 0; //�����̈�A�Ǎ��̈�
	private int SA = 0; //�Ǐ��p�X�^�[�g�A�h���X
	private int TMDL = 0; //�Ǎ����A������
	private int iCommNo = 0; //�ʐM�p�|�[�g�ԍ�
	private char[] memdata; //�������f�[�^�Ǐ��p
	
	//�Z�b�^�[
	public void setResult ( int result ) {
		Result = result;
	}
	public void setMB ( int mB ) {
		MB = mB;
	}
	public void setSA ( int sA ) {
		SA = sA;
	}
	public void setTMDL ( int tMDL ) {
		TMDL = tMDL;
	}
	public void setiCommNo ( int iCommNo ) {
		this.iCommNo = iCommNo;
	}
	
	//�Q�b�^�[
	public int getResult () {
		return Result;
	}
	public int getMB () {
		return MB;
	}
	public int getSA () {
		return SA;
	}
	public int getTMDL () {
		return TMDL;
	}
	public int getiCommNo () {
		return iCommNo;
	}
	
	static {
		System.loadLibrary ( "RFIDAPI" );
	}
	
	//�ڑ��p�֐�
	public void ConnectRFID () {
		System.out.println ( "RFID�ɐڑ����܂��@�ڑ����@��I��ł�������" );
		System.out.println ( "1.USB 2.LAN/WLAN" );
		try{			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //���̓X�g���[��
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) { //���͂�LAN��USB���𔻒f
		    case 1: {
		    	System.out.println( "USB�ɂ���Đڑ����܂�" );
		    	System.out.println( "�ڑ���......" );
		    	
		    	setiCommNo ( ConnectRFIDonUSB ( 0, 1, 0 ) ); //�����Őڑ�JNI�𓮂���
		    	
				if ( getiCommNo () > 0 ) {
					System.out.println( "�ڑ��ɐ������܂����@�ʐM�f�o�C�X�ԍ��� " + getiCommNo () + " �ł�" );
				}
				else System.out.println( "�ڑ��Ɏ��s���܂���" );
		    	
		    } break;
		    
		    case 2: {
		    	System.out.println( "LAN/WLAN�ɂ���Đڑ����܂��@�ڑ����IP�A�h���X����͂��Ă�������" );
		    	try {
				    String IP = br.readLine ();
				    System.out.println( "�ڑ��p�|�[�g�ԍ�����͂��Ă�������" );
				    String Port = br.readLine ();
				    System.out.println( "IP�A�h���X: "+ IP + "�|�[�g�F " + "�ɐڑ���......" );
				    setiCommNo ( ConnectRFIDonLAN ( 0, IP, Integer.parseInt ( Port ) ) ); //USB�Ȃ̂�010�ŌŒ�
					if ( getiCommNo () > 0 ) {
						System.out.println ( "�ڑ��ɐ������܂����@�ʐM�f�o�C�X�ԍ��� " + getiCommNo () + " �ł�" );
					}
					else System.out.println ( "�ڑ��Ɏ��s���܂���" );
		    	} catch ( IOException e ) {
		    		System.out.println ( e );
		    	}
		    } break;
		    
		    default: System.out.println ( "�������l����͂��Ă�������" ); break;
		    }
		    
		} catch ( IOException e ) {
			System.out.println ( e );
		} 
	}
	
	public void DisConnectRFID () {
		System.out.println ( "RFID�Ƃ̐ڑ����������܂�" );
		setResult ( DisconnectRFID ( getiCommNo () ) );
		   if ( getResult() == 0 ) {
		   	System.out.println( "�ڑ����������܂���" );
		   }
		   else System.out.println( "�ڑ������Ɏ��s���܂����B�@�ʐM�f�o�C�X�ԍ����m�F���Ă�������" );
	}
	
	public void ReedRFIDsMemory () {
		System.out.println ( "RFID�̃��������擾���܂�" );
		System.out.println ( "�擾�������̈��I��ł�������" );
		System.out.println ( "1.RFU 2.EPC 3.TID 4.User" );
		try {
			BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) ); //���̓X�g���[��
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) {
		    case 1: {
		    	System.out.println ( "RFU�̈悩��擾���܂�" );
		    	setMB ( 0 );
		    } break;
		    case 2: {
		    	System.out.println ( "EPC�̈悩��擾���܂�" );
		    	setMB ( 1 );
		    } break;
		    case 3: {
		    	System.out.println ( "TID�̈悩��擾���܂�" );
		    	setMB ( 2 );
		    } break;
		    case 4: {
		    	System.out.println ( "User�̈悩��擾���܂�" );
		    	setMB ( 3 );
		    } break;
		    default: System.out.println ( "�����������������Ă�������" ); break;
		    }
		    
		    System.out.println ( "�X�^�[�g�A�h���X���w�肵�Ă������� 0x00�Ԓn����ǂݍ��ނƂ���0�Ɠ��͂��Ă�������" );
		    setSA ( Integer.parseInt (br.readLine () ) );
		    
		    System.out.println ( "�ǂݍ��ݒ������w�肵�Ă�������(��byte���ǂނ�)" );
		    setTMDL ( Integer.parseInt ( br.readLine () ) );
		    
		    System.out.println ("�ǂݍ��݂��s���܂�......");
		    
		    memdata = new char[ getTMDL() ];
		    
			setResult ( ReadRFIDsMem ( iCommNo, getMB (), getSA (), getTMDL (), memdata ) );
			
			if ( getResult () == 0 ) {
				System.out.println( "�ǂݍ��݂ɐ������܂����I" );
				for ( int i=0; i < memdata.length; i++ ) System.out.print ( memdata[i] + " " );			
				System.out.println ();
			}
			else System.out.println ( "�ǂݍ��݂Ɏ��s���܂���" );
			
		} catch ( IOException e ) {
			System.out.println ( e );
		}
	}
	
	public void WriteRFIDsMemory () {
		System.out.println ( "RFID�̃������֏������s���܂�" );
		System.out.println ( "�������ޗ̈��I��ł�������" );
		System.out.println ( "1.RFU 2.EPC 3.TID 4.User" );
		try {
			BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) ); //���̓X�g���[��
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) {
		    case 1: {
		    	System.out.println ( "RFU�̈�֏������݂܂�" );
		    	setMB ( 0 );
		    } break;
		    case 2: {
		    	System.out.println ( "EPC�̈�֏������݂܂�" );
		    	setMB ( 1 );
		    } break;
		    case 3: {
		    	System.out.println ( "TID�̈�֏������݂܂�" );
		    	setMB ( 2 );
		    } break;
		    case 4: {
		    	System.out.println ( "User�̈�֏������݂܂�" );
		    	setMB ( 3 );
		    } break;
		    default: System.out.println ( "�����������������Ă�������" ); break;
		    }
		    
		    System.out.println ( "�X�^�[�g�A�h���X���w�肵�Ă������� 0x00�Ԓn���珑�����ނƂ���0�Ɠ��͂��Ă�������" );
		    setSA ( Integer.parseInt (br.readLine () ) );
		    
		    System.out.println ( "�������ޓ��e����͂����Ă��������i���p�p�����j" );
		    str = br.readLine ();
		    char[] charArray = str.toCharArray(); //�ǂݍ��񂾕�������P�������̕����ɕ���
		    setTMDL( charArray.length );
		    System.out.println ("�������݂��s���܂�......");
		    memdata = new char[ getTMDL() ];
		    for ( int i = 0; i < memdata.length; i++ ) memdata[i] = charArray[i];
		    
			setResult ( WriteRFIDsMem ( iCommNo, getMB (), getSA (), getTMDL (), memdata ) ); //�������݊֐���@��
			
			if ( getResult () == 0 ) System.out.println( "�������݂ɐ������܂����I" );			
			else System.out.println ( "�������݂Ɏ��s���܂���" );
			
		} catch ( IOException e ) {
			System.out.println ( e );
		}
	}
}


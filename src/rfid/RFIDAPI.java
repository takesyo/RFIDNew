package rfid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class RFIDAPI {
	public native int ConnectRFIDonUSB ( int iType, int iPort, long ISpeed );  //USB接続用JNI関数
	public native int ConnectRFIDonLAN ( int iType, String IPAddress, int iPort ); //LAN接続用JNI関数
	public native int DisconnectRFID ( int iCommNo ); //接続解除用JNI関数
	public native int ReadRFIDsMem ( int iCommNo, int mb, int sa, int TMDLength, char[] MemoryData ); //メモリ読込用JNI関数
	public native int WriteRFIDsMem ( int iCommNo, int mb, int sa, int TMDLength, char[] MemoryData ); //メモリ書込用JNI関数
	
	private int Result = 1; //成功判定用変数 	基本的に0になってほしい奴
	private int MB = 0; //書込領域、読込領域
	private int SA = 0; //読書用スタートアドレス
	private int TMDL = 0; //読込長、書込長
	private int iCommNo = 0; //通信用ポート番号
	private char[] memdata; //メモリデータ読書用
	
	//セッター
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
	
	//ゲッター
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
	
	//接続用関数
	public void ConnectRFID () {
		System.out.println ( "RFIDに接続します　接続方法を選んでください" );
		System.out.println ( "1.USB 2.LAN/WLAN" );
		try{			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //入力ストリーム
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) { //入力でLANかUSBかを判断
		    case 1: {
		    	System.out.println( "USBによって接続します" );
		    	System.out.println( "接続中......" );
		    	
		    	setiCommNo ( ConnectRFIDonUSB ( 0, 1, 0 ) ); //ここで接続JNIを動かす
		    	
				if ( getiCommNo () > 0 ) {
					System.out.println( "接続に成功しました　通信デバイス番号は " + getiCommNo () + " です" );
				}
				else System.out.println( "接続に失敗しました" );
		    	
		    } break;
		    
		    case 2: {
		    	System.out.println( "LAN/WLANによって接続します　接続先のIPアドレスを入力してください" );
		    	try {
				    String IP = br.readLine ();
				    System.out.println( "接続用ポート番号を入力してください" );
				    String Port = br.readLine ();
				    System.out.println( "IPアドレス: "+ IP + "ポート： " + "に接続中......" );
				    setiCommNo ( ConnectRFIDonLAN ( 0, IP, Integer.parseInt ( Port ) ) ); //USBなので010で固定
					if ( getiCommNo () > 0 ) {
						System.out.println ( "接続に成功しました　通信デバイス番号は " + getiCommNo () + " です" );
					}
					else System.out.println ( "接続に失敗しました" );
		    	} catch ( IOException e ) {
		    		System.out.println ( e );
		    	}
		    } break;
		    
		    default: System.out.println ( "正しい値を入力してください" ); break;
		    }
		    
		} catch ( IOException e ) {
			System.out.println ( e );
		} 
	}
	
	public void DisConnectRFID () {
		System.out.println ( "RFIDとの接続を解除します" );
		setResult ( DisconnectRFID ( getiCommNo () ) );
		   if ( getResult() == 0 ) {
		   	System.out.println( "接続を解除しました" );
		   }
		   else System.out.println( "接続解除に失敗しました。　通信デバイス番号を確認してください" );
	}
	
	public void ReedRFIDsMemory () {
		System.out.println ( "RFIDのメモリを取得します" );
		System.out.println ( "取得したい領域を選んでください" );
		System.out.println ( "1.RFU 2.EPC 3.TID 4.User" );
		try {
			BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) ); //入力ストリーム
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) {
		    case 1: {
		    	System.out.println ( "RFU領域から取得します" );
		    	setMB ( 0 );
		    } break;
		    case 2: {
		    	System.out.println ( "EPC領域から取得します" );
		    	setMB ( 1 );
		    } break;
		    case 3: {
		    	System.out.println ( "TID領域から取得します" );
		    	setMB ( 2 );
		    } break;
		    case 4: {
		    	System.out.println ( "User領域から取得します" );
		    	setMB ( 3 );
		    } break;
		    default: System.out.println ( "ただしい数字を入れてください" ); break;
		    }
		    
		    System.out.println ( "スタートアドレスを指定してください 0x00番地から読み込むときは0と入力してください" );
		    setSA ( Integer.parseInt (br.readLine () ) );
		    
		    System.out.println ( "読み込み長さを指定してください(何byte分読むか)" );
		    setTMDL ( Integer.parseInt ( br.readLine () ) );
		    
		    System.out.println ("読み込みを行います......");
		    
		    memdata = new char[ getTMDL() ];
		    
			setResult ( ReadRFIDsMem ( iCommNo, getMB (), getSA (), getTMDL (), memdata ) );
			
			if ( getResult () == 0 ) {
				System.out.println( "読み込みに成功しました！" );
				for ( int i=0; i < memdata.length; i++ ) System.out.print ( memdata[i] + " " );			
				System.out.println ();
			}
			else System.out.println ( "読み込みに失敗しました" );
			
		} catch ( IOException e ) {
			System.out.println ( e );
		}
	}
	
	public void WriteRFIDsMemory () {
		System.out.println ( "RFIDのメモリへ書込を行います" );
		System.out.println ( "書き込む領域を選んでください" );
		System.out.println ( "1.RFU 2.EPC 3.TID 4.User" );
		try {
			BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) ); //入力ストリーム
		    String str = br.readLine ();
		    
		    switch ( Integer.parseInt ( str ) ) {
		    case 1: {
		    	System.out.println ( "RFU領域へ書き込みます" );
		    	setMB ( 0 );
		    } break;
		    case 2: {
		    	System.out.println ( "EPC領域へ書き込みます" );
		    	setMB ( 1 );
		    } break;
		    case 3: {
		    	System.out.println ( "TID領域へ書き込みます" );
		    	setMB ( 2 );
		    } break;
		    case 4: {
		    	System.out.println ( "User領域へ書き込みます" );
		    	setMB ( 3 );
		    } break;
		    default: System.out.println ( "ただしい数字を入れてください" ); break;
		    }
		    
		    System.out.println ( "スタートアドレスを指定してください 0x00番地から書き込むときは0と入力してください" );
		    setSA ( Integer.parseInt (br.readLine () ) );
		    
		    System.out.println ( "書き込む内容を入力をしてください（半角英数字）" );
		    str = br.readLine ();
		    char[] charArray = str.toCharArray(); //読み込んだ文字列を１文字ずつの文字に分解
		    setTMDL( charArray.length );
		    System.out.println ("書き込みを行います......");
		    memdata = new char[ getTMDL() ];
		    for ( int i = 0; i < memdata.length; i++ ) memdata[i] = charArray[i];
		    
			setResult ( WriteRFIDsMem ( iCommNo, getMB (), getSA (), getTMDL (), memdata ) ); //書き込み関数を叩く
			
			if ( getResult () == 0 ) System.out.println( "書き込みに成功しました！" );			
			else System.out.println ( "書き込みに失敗しました" );
			
		} catch ( IOException e ) {
			System.out.println ( e );
		}
	}
}


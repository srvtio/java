/*--- 最新版 2016.8.8 ---*/
/*
すでにある多孔質の物体配置ファイルから，物体部分を減らしていって新しい多孔質配置を作るプログラム
/*
(--- 読み取りファイル ---)
KnudsenConf(Rho1_"+rho1+"0,Tau1_"+tau1+"0,pt "+iPattern1+").dat
iPattern1で読み取る多孔質配置のパターンを選択

(--- 出力ファイル ---)
Parameter().dat ... メイン計算に必要なパラメータを記述したファイル
KnudsenData().dat ... 多孔質の配置を決めるファイル
porous_conf.gp ... gnuplotで多孔質の配置図を確認するためのファイル

*/
import java.io.*;

public class ClusterNumber {

    public static void main(String[] args) {
	int incx,incy ;
	int iCellNumber1 = 32;
	int iCellNumber2 = 32;
	int iTotalCell   = iCellNumber1 * iCellNumber2;
	int iParallel    = 1;
	int seed = 1;
	int x1,x2;
	int itemp = 0;
	double Knudsen1      = 0.0 ;
	double Knudsen2      = 100.0 ;
	double dtemp         = 0.0;
	double FinalPorosity = 0.45;
	dtemp = (FinalPorosity*100.0); 
	itemp = (int)Math.round(dtemp); //四捨五入
	int iPattern     = seed*1000000 + 11600 + itemp;

	System.out.println(iPattern);

	int NumberCell[][] = new int[iCellNumber1][iCellNumber2];
	double Knudsen[][] = new double[iCellNumber1][iCellNumber2];

	int FillN[][] = new int[iCellNumber1][iCellNumber2];
	int FillC[][] = new int[iCellNumber1][iCellNumber2];
	int maxNum = (iCellNumber1+1)*(iCellNumber2+1);
	int iincx[] = new int[maxNum];
	int iincy[] = new int[maxNum];

	int Number = 0;
	int Cluster = 0;
	int BufNumber = 1;
	int iincyM = 0;
	int iincyP = 0;

	/*--- 初期化 ---*/
	for(incx=0 ; incx<iCellNumber1 ; incx++){
		for(incy=0 ; incy<iCellNumber2 ; incy++){
		    FillN[incx][incy] = 0;
		    FillC[incx][incy] = 0;
		}
	}
	for(int i=0 ; i<maxNum ; i++){
	    iincx[i] = -1;
	    iincy[i] = -1;
	}

	/*--- ファイルの読み取り ---*/
	try{
	    File readfile1 = new File("KnudsenData(CN1_"+iCellNumber1+",CN2_"+iCellNumber2+",pt "+iPattern+").dat") ;

	    FileReader fr1 = new FileReader(readfile1) ;
	    StreamTokenizer st1 = new StreamTokenizer(fr1) ; 

	    //多孔質配置のデータファイル読み取り
	    // nextToken()で次のデータを読み取る
	    for(incx=0 ; incx<iCellNumber1 ; incx++){
		for(incy=0 ; incy<iCellNumber2 ; incy++){
		    // ファイル1から読み取り
		    st1.nextToken() ;
		    x1 = (int)st1.nval ;
		    st1.nextToken() ;
		    x2 = (int)st1.nval ;
		    st1.nextToken() ;
		    Knudsen[incx][incy] = st1.nval ;
		}
	    }

	    fr1.close() ;

	}catch(Exception e){
	    System.out.println(e) ;
	}

	// クラスターの大きさを計算
	for(incx=0 ; incx<iCellNumber1 ; incx++){
		for(incy=0 ; incy<iCellNumber2 ; incy++){

		    // １つのクラスターの処理
		    if(Knudsen[incx][incy] == Knudsen1){
			if(FillC[incx][incy] == 0) {
			    Cluster = Cluster + 1;
			    Number = 1;
			    FillN[incx][incy] = Number;
			    FillC[incx][incy] = Cluster;
			    iincx[Number] = incx;
			    iincy[Number] = incy;			    
			
			    // 一つのクラスターのループ
			    for(BufNumber=1 ; BufNumber<(maxNum-1) ; BufNumber++){

			    	if(iincy[BufNumber] == 0){
			    	    iincyM = iCellNumber2 - 1;
			    	}else{
			    	    iincyM = iincy[BufNumber] - 1;
			    	}

			    	if(iincy[BufNumber] == iCellNumber2-1){
			    	    iincyP = 0;
			    	}else{
			    	    iincyP = iincy[BufNumber] + 1;
			    	}
			    

			    	//下
			    	if(Knudsen[iincx[BufNumber]][iincyM] == Knudsen1){
			    	    if(FillC[iincx[BufNumber]][iincyM] == 0){
			    		Number = Number + 1;
			    		FillN[iincx[BufNumber]][iincyM] = Number;
			    		FillC[iincx[BufNumber]][iincyM] = Cluster;
			    		iincx[Number] = iincx[BufNumber];
			    		iincy[Number] = iincyM;				
			    	    }
			    	}

			    	//上
			    	if(Knudsen[iincx[BufNumber]][iincyP] == Knudsen1){
			    	    if(FillC[iincx[BufNumber]][iincyP] == 0){
			    		Number = Number + 1;
			    		FillN[iincx[BufNumber]][iincyP] = Number;
			    		FillC[iincx[BufNumber]][iincyP] = Cluster;
			    		iincx[Number] = iincx[BufNumber];
			    		iincy[Number] = iincyP;				
			    	    }
			    	}

			    	//左
			    	if(iincx[BufNumber] > 0){
			    	    if(Knudsen[iincx[BufNumber]-1][iincy[BufNumber]] == Knudsen1){
			    		if(FillC[iincx[BufNumber]-1][iincy[BufNumber]] == 0){
			    		    Number = Number + 1;
			    		    FillN[iincx[BufNumber]-1][iincy[BufNumber]] = Number;
			    		    FillC[iincx[BufNumber]-1][iincy[BufNumber]] = Cluster;
			    		    iincx[Number] = iincx[BufNumber]-1;
			    		    iincy[Number] = iincy[BufNumber];
			    		}
			    	    }
			    	}

			    	//右
			    	if(iincx[BufNumber] < iCellNumber1-1){
			    	    if(Knudsen[iincx[BufNumber]+1][iincy[BufNumber]] == Knudsen1){
			    		if(FillC[iincx[BufNumber]+1][iincy[BufNumber]] == 0){
			    		    Number = Number + 1;
			    		    FillN[iincx[BufNumber]+1][iincy[BufNumber]] = Number;
			    		    FillC[iincx[BufNumber]+1][iincy[BufNumber]] = Cluster;
			    		    iincx[Number] = iincx[BufNumber]+1;
			    		    iincy[Number] = iincy[BufNumber];
			    		}
			    	    }
			    	}

			    	if(iincx[BufNumber+1]<0){
			    	    break;
			    	}

			    }// 一つのクラスターのループ

			}
		    }// １つのクラスターの処理

		    Number = 0;
		    
		    for(int i=0 ; i<maxNum ; i++){
			iincx[i] = -1;
			iincy[i] = -1;
		    }
		    
		}
	}


	/*--- ファイルへの書き込み ---*/
	try{
	    File file1 = new File("test.dat") ;
	    
	    File file3 = new File("porous_conf.gp") ;

	    PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(file1))) ;
	    
	    PrintWriter pw3 = new PrintWriter(new BufferedWriter(new FileWriter(file3))) ;

	    //多孔質配置のデータファイル作成
	    for(incx=0 ; incx<iCellNumber1 ; incx++){
		for(incy=0 ; incy<iCellNumber2 ; incy++){
		    pw1.printf("%8d", incx);
		    pw1.printf("%8d", incy);
		    pw1.printf("%8d", FillC[incx][incy]);
		    pw1.printf("%8d %n", FillN[incx][incy]);	
		}
		pw1.printf("%8s %n", "  ") ;
	    }

	    //gnuplot作成ファイルの作成
	    pw3.printf("set terminal png %n") ;
	    pw3.printf("set pm3d map %n") ;
	    pw3.printf("set pm3d corners2color c2 %n") ;
	    pw3.printf("set size ratio 1 %n") ;
	    pw3.printf("set output '"+iPattern+".png' %n") ;
	    pw3.printf("unset colorbox %n") ;
	    pw3.printf("set palette rgbformulae 22,13,-31 %n") ;
	    pw3.printf("set xrange [0:31] %n") ;
	    pw3.printf("set yrange [0:31] %n") ;
	    pw3.printf("set xlabel 'x1' %n") ;
	    pw3.printf("set ylabel 'x2' %n") ;
	    pw3.printf("splot 'KnudsenData(CN1_"+iCellNumber1+",CN2_"+iCellNumber2+",pt "+iPattern+").dat'u 1:2:3  palette notitle %n") ;
	    pw3.printf("unset output %n") ;
	    pw3.printf("reset %n") ;

	    pw1.close() ;
	    pw3.close() ;

	}catch(IOException e){
	    System.out.println(e) ;
	}
    }
}

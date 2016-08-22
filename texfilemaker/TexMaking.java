/*--- 最新版 2016.8.16 ---*/
/*
任意のファルダにこのプログラムを置き，実行すると，コード中で指定したフォルダに置いてある
フォルダの一覧を取得し，各フォルダのPorousDataRaw().datファイルから必要なデータを読み取って
１つのファイルにまとめるプログラム
/*
(--- 読み取りファイル ---)
各フォルダの
Parameter().dat
PorousDataRaw().dat

(--- 出力ファイル ---)
flux-porosity.dat ... 流量と空隙率の関係を出力するファイル
datalist.dat ... ほしいデータの一覧を出力するファイル

*/
import java.io.*;
import java.util.*;

public class TexMaking {

    public static void main(String[] args) {
	int iCellNumber1 = 32 ;
	int iCellNumber2 = 32 ;
	int seed         = 0;
	int itemp        = 0;
	int KnudsenPt     ;
	int iParallel    = 1 ;
	int iPattern     = 0;
	double l1 = 1.0;
	double rho1 = 0.00;
	double tau1 = 1.00;
	double grad_p = (rho1*tau1 - 1.0)/l1/2.0;
	
	double porosity      = 0.0;
	double Knudsen1      = 0.0;
	double Knudsen2      = 100.0;
	double dtemp         = 0.0;
	double FinalPorosity = 0.0;

	double average_p_sub = 0.0;
	double sta_Fp = 0.0;
	double sta_Fm = 0.0;
	double por_area = 0.0;
	double mom_r = 0.0;
	double mom_l = 0.0;

	/*--- ファイルへの書き込み ---*/
	try{
	    File file1 = new File("data.tex") ;
	    PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(file1))) ;

	    //texファイルの作成
	    pw1.printf("\\documentclass[10pt]{jsarticle} %n");

	    pw1.printf("\\usepackage{amsmath,amssymb} %n");
	    pw1.printf("\\usepackage[dvips]{graphicx} %n");
	    pw1.printf("\\usepackage{overpic} %n");
	    pw1.printf("\\usepackage{bm} %n");
	    pw1.printf("\\usepackage{fancyhdr} %n");
	    pw1.printf("\\usepackage{cases} %n");

	    pw1.printf("\\pagestyle{fancy} %n");

	    pw1.printf("\\lhead{笠原史禎} %n");
	    pw1.printf("\\chead{} %n");
	    pw1.printf("\\rhead{\\today} %n");

	    pw1.printf("\\numberwithin{equation}{section} %n");

	    pw1.printf("\\newcommand{\\del}{\\partial} %n");

	    pw1.printf("\\begin{document} %n");

	    //ファイル名の一覧を取得する
	    File folder = new File("/home/kasahara/study/scatterer/random/8.10");
	    File files[] = folder.listFiles();

	    // ファイル名をソート
	    Arrays.sort(files, new Comparator<File>() {
		    public int compare(File file1, File file2){
			return file1.getName().compareTo(file2.getName());
		    }
		});
	    if( files != null ){
		for( int i = 0 ; i < files.length ; i++ ){
		    System.out.println(files[i].getName());
		}
	    }
	    else {
		System.out.println("files is null.");
	    }

	    //取得した一覧を表示する
	    for (int i=0; i<files.length; i++) {
		System.out.println("ファイル" + (i+1) + "→" + files[i]);

		/*--- ファイルの読み取り ---*/
		try{
		    // ファイル0から読み取り
		    File readfile0 = new File(files[i]+"/Parameter(CN1_"+iCellNumber1+",CN2_"+iCellNumber2+").dat") ;
		    FileReader fr0 = new FileReader(readfile0) ;
		    StreamTokenizer st0 = new StreamTokenizer(fr0) ; 

		    st0.nextToken();
		    KnudsenPt = (int)st0.nval;
		    st0.nextToken();
		    iParallel = (int)st0.nval;
		    st0.nextToken();
		    iPattern = (int)st0.nval;
		    st0.nextToken();
		    porosity = st0.nval;

		    // seedとFinalporosityの値を計算
		    seed = iPattern / 1000000;
		    itemp = iPattern - (seed*1000000 + 11600);
		    FinalPorosity = (double)itemp / 100.0;
		  
		    // ファイル1から読み取り
		    File readfile1 = new File(files[i]+"/PorousDataRaw(pt "+iPattern+",dpdx_"+grad_p+"0,poro_"+FinalPorosity+").dat") ;
		    FileReader fr1 = new FileReader(readfile1) ;
		    StreamTokenizer st1 = new StreamTokenizer(fr1) ; 

		    st1.nextToken() ;
		    Knudsen1 = st1.nval ;
		    st1.nextToken() ;
		    Knudsen2 = st1.nval ;
		    st1.nextToken() ;
		    double total_kp1_n = st1.nval ;
		    st1.nextToken() ;
		    double subporosity = st1.nval ;
		    
		    st1.nextToken() ;
		    grad_p = st1.nval ;
		    st1.nextToken() ;
		    porosity = st1.nval ;
		    st1.nextToken() ;
		    double sta_F = st1.nval ;
		    st1.nextToken() ;
		    sta_Fp = st1.nval ;
		    st1.nextToken() ;
		    sta_Fm = st1.nval ;
		    st1.nextToken() ;
		    por_area = st1.nval ;

		    st1.nextToken() ;
		    mom_l = st1.nval ;
		    st1.nextToken() ;
		    mom_r = st1.nval ;
		    st1.nextToken() ;
		    double mom_t = st1.nval ;
		    st1.nextToken() ;
		    double mom_b = st1.nval ;
		    st1.nextToken() ;
		    double mom_total = st1.nval ;
		    st1.nextToken() ;
		    double p_l = st1.nval ;
		    st1.nextToken() ;
		    double p_r = st1.nval ;
		    st1.nextToken() ;
		    double p_sub = st1.nval ;
		    st1.nextToken() ;
		    double p_t = st1.nval ; 
		    st1.nextToken() ;
		    double p_b = st1.nval ;
		    st1.nextToken() ;
		    double p_total = st1.nval ;
		    st1.nextToken() ;
		    double mom_p_total = st1.nval ;

		    st1.nextToken() ;
		    double rho_r = st1.nval ;
		    st1.nextToken() ;
		    double t_r = st1.nval ;

		    st1.nextToken() ;
		    double average_p_left = st1.nval ;
		    st1.nextToken() ;
		    double average_p_right = st1.nval ;
		    st1.nextToken() ;
		    average_p_sub = st1.nval ;

		    // 速度データはまだ読み取らない

		    fr1.close() ;

		}catch(Exception e){
		    System.out.println(e) ;
		}//読み取りの例外

		//texファイルの作成
		if(i%4==0){
		    pw1.printf("\\begin{figure}[t] %n");
		    pw1.printf("\\begin{center}    %n");
		    // pw1.printf("\\vspace{-3.5cm} %n");
		    pw1.printf("\\vspace{-1.0cm} %n");
		}

		if(i%2==0){
		pw1.printf("\\begin{tabular}{cc} %n");
		// pw1.printf("\\hspace{-7cm} %n");
		pw1.printf("\\hspace{-5cm} %n");
		}
	
		// pw1.printf("\\begin{minipage}{1.0\\textwidth}   %n");

		// pw1.printf("\\begin{overpic}[width=1.0\\hsize]{"+files[i]+"/vecmap.eps}   %n");   
		// pw1.printf("\\put(44,2){\\large ("+(i+1)+")$\\epsilon = "+porosity+"$} %n");
		// pw1.printf("\\end{overpic}       %n");
		// pw1.printf("\\vspace{-1cm}       %n");
		// pw1.printf("\\end{minipage}  %n");
      
		// pw1.printf("\\hspace{-8cm} %n");

		pw1.printf("\\begin{minipage}{1.0\\textwidth}   %n");

		pw1.printf("\\begin{overpic}[width=0.82\\hsize]{"+files[i]+"/ClusterSize_"+iPattern+".eps}   %n");   
		pw1.printf("\\put(44,-4){\\large ("+(i+1)+")$\\epsilon = "+porosity+"$} %n");
		pw1.printf("\\end{overpic}       %n");
		// pw1.printf("\\vspace{-1cm}       %n");
		pw1.printf("\\vspace{1cm}       %n");
		pw1.printf("\\end{minipage}  %n");

		// pw1.printf("\\hspace{-7cm} %n");
		pw1.printf("\\hspace{-8cm} %n");

		if(i%2==1){
		pw1.printf("\\end{tabular} %n");
		
		// pw1.printf("\\vspace{-2cm} %n");
		}

		if(i%4==3){
		    // pw1.printf("\\vspace{5cm} %n");
		    pw1.printf("\\vspace{1cm} %n");
		    pw1.printf("\\caption{\\large $\\hat{p}_0=1,\\hat{p}_1="+rho1*tau1+"$，物体サイズ$d=l_1/16$} %n");
		    pw1.printf("\\label{fig:profile3} %n");
		    pw1.printf("\\end{center} %n");
		    pw1.printf("\\vspace{-10mm} %n");
		    pw1.printf("\\end{figure} %n");
		    pw1.printf("\\clearpage %n");
		}

		if(i%4!=3 & i==files.length-1){
		    // pw1.printf("\\vspace{5cm} %n");
		    pw1.printf("\\vspace{1cm} %n");
		    pw1.printf("\\caption{\\large $\\hat{p}_0=1,\\hat{p}_1="+rho1*tau1+"$，物体サイズ$d=l_1/16$} %n");
		    pw1.printf("\\label{fig:profile3} %n");
		    pw1.printf("\\end{center} %n");
		    pw1.printf("\\vspace{-10mm} %n");
		    pw1.printf("\\end{figure} %n");
		    pw1.printf("\\clearpage %n");
		}

	    }//フォルダごとの処理
	   
	    pw1.printf("\\end{document} %n");

	    pw1.close();

	}catch(IOException e){
	    System.out.println(e) ;
	}//書き込みの例外
    }
}

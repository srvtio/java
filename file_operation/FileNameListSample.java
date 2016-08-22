import java.io.File;

public class FileNameListSample {

    public static void main(String args[]) {

        //ファイル名の一覧を取得する
        File file = new File("/home/kasahara/study/scatterer/random/8.10");
        File files[] = file.listFiles();

        //取得した一覧を表示する
        for (int i=0; i<files.length; i++) {
            System.out.println("ファイル" + (i+1) + "→" + files[i]);
        }

    }

}

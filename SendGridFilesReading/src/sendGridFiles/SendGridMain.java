package sendGridFiles;

public class SendGridMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SendGridFilesSerivice sgf=new SendGridFilesSerivice();
		
		WriteCTAFiles wpcdf=new WriteCTAFiles(sgf);
		
		sgf.setWritePeCeDealersFiles(wpcdf);
		
		sgf.fileDir();
		
		wpcdf.loadPeCeDeMails();
		
		wpcdf.writePeCeDeMails();

	}

}

package gui;

public interface IProgressListener {

	public void advanceProcess(int nPages, int currentPage);

	public void finishProcess(String string);

}

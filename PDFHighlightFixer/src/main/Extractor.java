package main;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.util.PDFTextStripperByArea;

import gui.IProgressListener;

public class Extractor implements Runnable {

	private List<IProgressListener> progressListeners;

	private File in;
	private File out;
	private boolean stopped;
	private boolean includeHighlights;
	private boolean includeUnderlines;
	private boolean overWriteHighlights;
	private boolean overWriteUnderlines;
	private int start;
	private int finish;

	public static final int FINISH = -1;

	public Extractor(File in, File out, boolean includeHighlights, boolean includeUnderlines, boolean overWHighlights,
			boolean overWUnderlines, int start, int finish) {
		this.in = in;
		this.out = out;
		this.includeHighlights = includeHighlights;
		this.includeUnderlines = includeUnderlines;
		this.overWriteHighlights = overWHighlights;
		this.overWriteUnderlines = overWUnderlines;
		this.start = start;
		this.finish = finish;
	}

	public void run() {
		PDDocument pddDocument = null;
		stopped = false;
		try {
			pddDocument = PDDocument.load(in);
			List<PDPage> allPages = new ArrayList<PDPage>();
			pddDocument.getDocumentCatalog().getPages().getAllKids(allPages);

			finish = finish == FINISH ? allPages.size() : finish;
			if (start <= 0 || allPages.size() < (finish - 1)) {
				for (IProgressListener listener : progressListeners)
					listener.finishProcess("please provide a suitable page interval");
				return;
			}
			List<PDPage> selectedPages = allPages.subList(start - 1, finish - 1);

			int nPages = selectedPages.size();
			int i = 1;
			for (PDPage page : selectedPages) {
				if (stopped)
					break;
				advanceProcess(nPages, i++);
				List<PDAnnotation> la = page.getAnnotations();
				for (PDAnnotation anot : la) {
					if (stopped)
						break;
					if (anot instanceof PDAnnotationTextMarkup && ((includeHighlights
							&& anot.getSubtype().equals(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT))
							|| (includeUnderlines
									&& anot.getSubtype().equals(PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE))))
						processHighlight((PDAnnotationTextMarkup) anot, page);
				}
				pddDocument.save(out);
			}
			pddDocument.close();
			for (IProgressListener listener : progressListeners)
				listener.finishProcess("Process completed");
		} catch (Exception ex) {
			for (IProgressListener listener : progressListeners)
				listener.finishProcess("Error processing the document|");
		}

	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public void addProgressListener(IProgressListener pl) {
		if (progressListeners == null)
			progressListeners = new LinkedList<IProgressListener>();
		progressListeners.add(pl);
	}

	private void advanceProcess(int nPages, int currentPage) {
		for (IProgressListener listerners : progressListeners)
			listerners.advanceProcess(nPages, currentPage);
	}

	private void processHighlight(PDAnnotationTextMarkup highlight, PDPage page) throws IOException {
		if ((highlight.getContents() != null && !highlight.getContents().isEmpty())
				&& ((highlight.getSubtype().equals(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT) && !overWriteHighlights)
						|| (highlight.getSubtype().equals(PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE)
								&& !overWriteUnderlines)))
			return;
		float[] quads = highlight.getQuadPoints();
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		PDRectangle pagesize = page.findMediaBox();
		for (int i = 0; i < quads.length; i += 8) {
			stripper.setSortByPosition(true);
			Rectangle2D.Float rect = new Rectangle2D.Float(quads[i] - 1, pagesize.getHeight() - quads[i + 1] - 1,
					quads[i + 6] - quads[i] + 1, quads[i + 1] - quads[i + 7] + 1);
			stripper.addRegion("" + i, rect);

		}
		stripper.extractRegions(page);
		List<String> lines = new LinkedList<String>();
		for (String region : stripper.getRegions())
			lines.add(stripper.getTextForRegion(region));

		// Format text and set it as comment of the annotation
		String highlightText = "";
		for (String line : lines)
			highlightText = highlightText + line;
		System.out.println(highlightText);
		highlight.setContents(highlightText);
	}

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ThumbnailService;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
public class ThumbnailServiceImpl implements ThumbnailService {

  private static final String MIME_DOCX =
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

  public static final String THUMBNAIL_CONTENT_TYPE = "image/jpg";

  private static final String MIME_PREFIX_IMAGE = "image/";

  private static final String MIME_APPLICATION_PDF = "application/pdf";

  private static final int OUTPUT_DPI = 150;

  private static final int OUTPUT_SIZE = 200;

  private static final String OUTPUT_TYPE_JPG = "jpg";

  private static final String MIME_TYPE = "mimeType";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Override
  public boolean possibileGenerazioneThumbnail(String mimeType) {
    ValidationUtils.assertNotNull(mimeType, MIME_TYPE);

    Callable<Boolean> generatore = getGeneratore(null, mimeType, null);
    return generatore != null;
  }

  @Override
  public boolean generaThumbnail(InputStream input, String mimeType, OutputStream output) {
    final var method = "generaThumbnail";
    logger.debug(method, "richiesta generazione anteprima per file di tipo {}", mimeType);

    ValidationUtils.assertNotNull(input, "input");
    ValidationUtils.assertNotNull(mimeType, MIME_TYPE);
    ValidationUtils.assertNotNull(output, "output");

    Callable<Boolean> generatore = getGeneratore(input, mimeType, output);
    if (generatore != null) {
      return attempt(() -> generatore.call()).booleanValue();
    }

    return false;
  }

  private Callable<Boolean> getGeneratore(InputStream input, String mimeType, OutputStream output) {
    ValidationUtils.assertNotNull(mimeType, MIME_TYPE);
    mimeType = mimeType.toLowerCase();

    if (mimeType.equals(MIME_APPLICATION_PDF)) {
      return () -> generaThumbnailPDF(input, output, OUTPUT_DPI, ImageType.RGB, OUTPUT_TYPE_JPG);
    }

    if (mimeType.startsWith(MIME_PREFIX_IMAGE)) {
      return () -> generaThumbnailImmagine(input, output, OUTPUT_SIZE, BufferedImage.TYPE_INT_RGB,
          OUTPUT_TYPE_JPG);
    }

    if (mimeType.startsWith(MIME_DOCX)) {
      return () -> generaThumbnailDocx(input, output, OUTPUT_DPI, ImageType.RGB, OUTPUT_TYPE_JPG);
    }

    return null;
  }

  private boolean generaThumbnailImmagine(InputStream input, OutputStream output, int outputSize,
      int imageType, String outputType) throws IOException {

    BufferedImage bimg = new BufferedImage(outputSize, outputSize, imageType);

    bimg.createGraphics().drawImage(
        ImageIO.read(input).getScaledInstance(outputSize, outputSize, Image.SCALE_SMOOTH), 0, 0,
        null);

    ImageIO.write(bimg, outputType, output);
    return true;
  }

  private boolean generaThumbnailDocx(InputStream input, OutputStream output, int dpi,
      ImageType imageType, String outputType) throws IOException {

    XWPFDocument document = new XWPFDocument(input);
    PdfOptions options = PdfOptions.create();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PdfConverter.getInstance().convert(document, out, options);

    ByteArrayInputStream otherInput = new ByteArrayInputStream(out.toByteArray());
    return generaThumbnailPDF(otherInput, output, dpi, imageType, outputType);
  }

  private boolean generaThumbnailPDF(InputStream input, OutputStream output, int dpi,
      ImageType imageType, String outputType) throws IOException {
    try (PDDocument document = PDDocument.load(input)) {
      PDFRenderer pdfRenderer = new PDFRenderer(document);
      if (document.getNumberOfPages() > 0) {
        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, dpi, imageType);
        ImageIO.write(bim, outputType, output);
        return true;
      } else {
        return false;
      }
    }
  }

  private <T> T attempt(Callable<T> task) {
    try {
      return task.call();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerException(
          "Errore nella generazione dell'anteprima: " + e.getMessage(), e);
    }
  }
}

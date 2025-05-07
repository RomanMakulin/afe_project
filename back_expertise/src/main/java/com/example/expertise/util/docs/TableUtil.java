package com.example.expertise.util.docs;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Утилитный класс для работы с таблицами в документах Word.
 */
@Component
public class TableUtil {

    private final ObjectFactory factory;

    // Константы для таблиц и изображений
    public static final int DEFAULT_TABLE_WIDTH_TWIPS = 9000;
    public static final int DEFAULT_SCREEN_MAP_WIDTH = 9750;
    public static final String DEFAULT_WIDTH_TYPE = "dxa";
    public static final int DEFAULT_BORDER_SIZE = 4; // 0.5 pt
    public static final String DEFAULT_BORDER_COLOR = "000000";
    public static final STBorder DEFAULT_BORDER_STYLE = STBorder.SINGLE;
    public static final int DEFAULT_IMAGE_WIDTH_TWIPS = 4500;
    private static final int SPACING_TOP_IMAGE = 120; // 6 pt в TWIPS
    private static final int SPACING_BOTTOM_IMAGE = 120; // 6 pt в TWIPS
    private static final int SPACING_TOP_CAPTION = 60; // 3 pt в TWIPS
    private static final int SPACING_BOTTOM_CAPTION = 120; // 6 pt в TWIPS

    public TableUtil() {
        this.factory = new ObjectFactory();
    }

    /**
     * Создает таблицу с заданными параметрами.
     */
    public Tbl createTable(int widthTwips, String widthType, int borderSize, String borderColor, STBorder borderStyle) {
        Tbl table = factory.createTbl();
        TblPr tblPr = factory.createTblPr();
        setTableWidth(tblPr, widthTwips, widthType);
        setTableBorders(tblPr, borderSize, borderColor, borderStyle);
        table.setTblPr(tblPr);
        return table;
    }

    /**
     * Создает строку таблицы.
     */
    public Tr createTableRow() {
        return factory.createTr();
    }

    /**
     * Создает ячейку таблицы с изображением и подписью, центрированную по горизонтали и вертикали с отступами.
     */
    public Tc createImageCell(WordprocessingMLPackage wordPackage, byte[] imageData, int imageWidth, String caption) throws Exception {
        Tc tableCell = factory.createTc();

        // Вертикальное выравнивание ячейки по центру
        TcPr tableCellProperties = factory.createTcPr();
        CTVerticalJc verticalAlignment = factory.createCTVerticalJc();
        verticalAlignment.setVal(STVerticalJc.CENTER);
        tableCellProperties.setVAlign(verticalAlignment);
        tableCell.setTcPr(tableCellProperties);

        // Параграф с изображением
        P imageParagraph = createCenteredParagraphWithSpacing(
                createImageRun(wordPackage, imageData, null, imageWidth),
                SPACING_TOP_IMAGE, SPACING_BOTTOM_IMAGE
        );
        tableCell.getContent().add(imageParagraph);

        // Подпись, если указана
        if (caption != null && !caption.isEmpty()) {
            P captionParagraph = createCenteredParagraphWithSpacing(
                    createTextRun(caption),
                    SPACING_TOP_CAPTION, SPACING_BOTTOM_CAPTION
            );
            tableCell.getContent().add(captionParagraph);
        }

        return tableCell;
    }

    /**
     * Создает пустую ячейку таблицы с вертикальным выравниванием по центру.
     */
    public Tc createEmptyCell() {
        Tc emptyCell = factory.createTc();
        TcPr tableCellProperties = factory.createTcPr();
        CTVerticalJc verticalAlignment = factory.createCTVerticalJc();
        verticalAlignment.setVal(STVerticalJc.CENTER);
        tableCellProperties.setVAlign(verticalAlignment);
        emptyCell.setTcPr(tableCellProperties);
        emptyCell.getContent().add(factory.createP());
        return emptyCell;
    }

    /**
     * Создает run с изображением.
     */
    public R createImageRun(WordprocessingMLPackage wordPackage, byte[] imageData, String altText, int imageWidth) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordPackage, imageData);
        Inline inline = imagePart.createImageInline(null, altText, 0, 1, false, imageWidth);
        R imageRun = factory.createR();
        Drawing drawing = factory

                .createDrawing();
        drawing.getAnchorOrInline().add(inline);
        imageRun.getContent().add(drawing);
        return imageRun;
    }

    /**
     * Добавляет строку с текстом в таблицу.
     */
    public void addTableRow(Tbl table, String text) {
        Tr row = createTableRow();
        Tc cell = createEmptyCell();
        P paragraph = factory.createP();
        R run = factory.createR();
        Text textObj = factory.createText();
        textObj.setValue(text);
        run.getContent().add(textObj);
        paragraph.getContent().add(run);
        cell.getContent().set(0, paragraph); // Заменяем пустой параграф
        row.getContent().add(cell);
        table.getContent().add(row);
    }

    // Вспомогательные методы

    private void setTableWidth(TblPr tblPr, int widthTwips, String widthType) {
        TblWidth tblWidth = factory.createTblWidth();
        tblWidth.setW(BigInteger.valueOf(widthTwips));
        tblWidth.setType(widthType);
        tblPr.setTblW(tblWidth);
    }

    private void setTableBorders(TblPr tblPr, int borderSize, String borderColor, STBorder borderStyle) {
        TblBorders borders = factory.createTblBorders();
        CTBorder border = factory.createCTBorder();
        border.setVal(borderStyle);
        border.setSz(BigInteger.valueOf(borderSize));
        border.setSpace(BigInteger.ZERO);
        border.setColor(borderColor);
        borders.setTop(border);
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        tblPr.setTblBorders(borders);
    }

    private P createCenteredParagraphWithSpacing(R content, int spacingTop, int spacingBottom) {
        P paragraph = factory.createP();
        paragraph.getContent().add(content);

        PPr pPr = factory.createPPr();
        Jc justification = factory.createJc();
        justification.setVal(JcEnumeration.CENTER);
        pPr.setJc(justification);

        PPrBase.Spacing spacing = factory.createPPrBaseSpacing();
        spacing.setBefore(BigInteger.valueOf(spacingTop));
        spacing.setAfter(BigInteger.valueOf(spacingBottom));
        pPr.setSpacing(spacing);

        paragraph.setPPr(pPr);
        return paragraph;
    }

    private R createTextRun(String text) {
        R run = factory.createR();
        Text textObj = factory.createText();
        textObj.setValue(text);
        run.getContent().add(textObj);
        return run;
    }
}
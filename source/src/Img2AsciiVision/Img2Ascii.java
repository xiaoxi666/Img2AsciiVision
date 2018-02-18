package Img2AsciiVision;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Img2Ascii {
	public static void createImg2AsciiFromPic(String folderForResult, String fileAbsolutePath, double scale)
			throws IOException {
		int index = fileAbsolutePath.lastIndexOf("\\");
		String fileName = fileAbsolutePath.substring(index + 1);

		File outputFilePath = new File(folderForResult + "/" + fileName + ".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
		final String BaseCharacter = "M&$B%0eol1v!'=+;:.";// "#&$B%eaovl+;:. ";//"B&eavoL!,. ";//" //
															// #&$%*o!;.";//"M&Dn%l+-,. "//MN&$Beon%vl+;:.
		try {
			final BufferedImage image = ImageIO.read(new File(fileAbsolutePath));
			int heightOfImg = (int) Math.ceil(image.getHeight());
			int widthOfImg = (int) Math.ceil(image.getWidth());

			for (int y = 0; y < heightOfImg; y += (2 / scale)) {
				for (int x = 0; x < widthOfImg; x += 1 / scale) {
					final int pixel = image.getRGB(x, y);
					final int a = (pixel >> 24) & 0xff;// alpha通道，0为全透明
					String resultCharacters;
					if (a > 10) {
						int r = (pixel >> 16) & 0xff;
						int g = (pixel >> 8) & 0xff;
						int b = pixel & 0xff;
						float gray = 0.299f * r + 0.578f * g + 0.114f * b;
						int indexOfBaseCharacter = Math.round(gray * (BaseCharacter.length() + 1) / 255);
						resultCharacters = indexOfBaseCharacter >= BaseCharacter.length() ? " "
								: String.valueOf(BaseCharacter.charAt(indexOfBaseCharacter));
					} else {
						resultCharacters = " ";// 透明部分用空格填充
					}
					writer.write(resultCharacters);
				}
				writer.write("\n");
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createImg2AsciiFromFolder(String folderPath, double scale) throws IOException {

		String currentTime = DateFormat.getDateTimeInstance().format(new Date()).toString().replace("-", "_")
				.replace(" ", "_").replace(":", "_");
		File folderForResult = new File(folderPath + "/字符画转换结果" + currentTime);
		if (!folderForResult.exists()) {
			folderForResult.mkdir();
		}

		File root = new File(folderPath);
		File[] files = root.listFiles();

		int imgConverted = 0;

		for (File file : files) {
			if (!file.isDirectory()) {
				String ImgAbsolutePath = file.getAbsolutePath();
				int index = ImgAbsolutePath.lastIndexOf(".");
				String extensionName = ImgAbsolutePath.substring(index + 1);
				if ("jpg".equalsIgnoreCase(extensionName) || "jpeg".equalsIgnoreCase(extensionName)
						|| "gif".equalsIgnoreCase(extensionName) || "png".equalsIgnoreCase(extensionName)
						|| "bmp".equalsIgnoreCase(extensionName)) {
					createImg2AsciiFromPic(folderForResult.toString(), ImgAbsolutePath, scale);
					++imgConverted;
				}
			}
		}
		if (imgConverted > 0) {
			JOptionPane.showMessageDialog(null, imgConverted + " 张图片转换成功，已保存至文件夹 “" + folderForResult.toString() + "”");
		} else {
			folderForResult.delete();
			JOptionPane.showMessageDialog(null, "所选文件夹不包含图片！");
		}
	}

	public static void startConvert(String pathSelected, boolean FileOrFolder, double scale) throws IOException {
		if (FileOrFolder) {
			createImg2AsciiFromFolder(pathSelected, scale);
		} else {
			int index = pathSelected.lastIndexOf("\\");
			String folderPath = pathSelected.substring(0, index);

			String currentTime = DateFormat.getDateTimeInstance().format(new Date()).toString().replace("-", "_")
					.replace(" ", "_").replace(":", "_");
			File folderForResult = new File(folderPath + "/字符画转换结果" + currentTime);
			if (!folderForResult.exists()) {
				folderForResult.mkdir();
			}
			createImg2AsciiFromPic(folderForResult.toString(), pathSelected, scale);
			JOptionPane.showMessageDialog(null, "1 张图片转换成功，已保存至文件夹 “" + folderForResult.toString() + "”");
		}
		// System.out.println("convert done.");
	}
}

package Img2AsciiVision;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

@SuppressWarnings("serial")
public class MainWindows extends JFrame {
	private static JFrame frame;
	private JPanel panelBefore, panelDrag, panelControl;
	private JButton openItem, openFolder, convertImg;
	private JLabel labelScale;
	private JCheckBox buttonHolderWindowsAbove;

	String pathSelected;
	boolean FileOrFolder = false;// false:file; true:folder

	double scale = 1;// 缩放因子支持scale=0.1;0.2;0.25;0.5;1五种比例，1为不缩放

	public MainWindows() {
		Container container = this.getContentPane();

		panelBefore = new JPanel();
		Border etchedBefore = BorderFactory.createEtchedBorder();
		Border titleBefore = BorderFactory.createTitledBorder(etchedBefore, "待处理图片");
		panelBefore.setBorder(titleBefore);

		openItem = new JButton("选择文件");
		openItem.setFont(new Font("华文楷体", Font.PLAIN, 15));
		openItem.setSize(10, 20);
		openItem.addActionListener(event -> {
			convertImg.setEnabled(false);
			JFileChooser imgDlg = new JFileChooser();
			imgDlg.setDialogTitle("请选择待转换文件");
			FileFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "gif", "png","bmp");
			imgDlg.setFileFilter(filter);
			imgDlg.setAccessory(new ImagePreviewer(imgDlg));
			imgDlg.setFileView(new FileIconView(filter, new ImageIcon("./res/palette.gif")));
			int result = imgDlg.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				String imgPath = imgDlg.getSelectedFile().getPath();
				pathSelected = imgPath;
				FileOrFolder = false;
				checkIfIsImgThenEnableConvert();
			}
		});
		panelBefore.add(openItem);

		openFolder = new JButton("添加文件夹");
		openFolder.setFont(new Font("华文楷体", Font.PLAIN, 15));
		openFolder.setToolTipText("将处理选定文件夹下的所有图片文件");
		openFolder.addActionListener(event -> {
			convertImg.setEnabled(false);
			JFileChooser imgDlg = new JFileChooser();
			imgDlg.setDialogTitle("请选择文件夹");
			imgDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = imgDlg.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				String folderPath = imgDlg.getSelectedFile().toString();// .getPath();
				pathSelected = folderPath;
				FileOrFolder = true;
				convertImg.setEnabled(true);
			}
		});
		panelBefore.add(openFolder);

		convertImg = new JButton("开始转换");
		convertImg.setFont(new Font("华文楷体", Font.PLAIN, 15));
		convertImg.setEnabled(false);
		convertImg.addActionListener(event -> {
			try {
				Img2Ascii.startConvert(pathSelected, FileOrFolder, scale);
				convertImg.setEnabled(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		panelBefore.add(convertImg);

		panelDrag = new JPanel();
		Border etchedDrag = BorderFactory.createEtchedBorder();
		Border titleDrag = BorderFactory.createTitledBorder(etchedDrag, "支持拖放");
		JTextArea imgDropArea = new JTextArea("    拖放单张图片到此处    ");
		imgDropArea.setFont(new Font("华文楷体", Font.PLAIN, 30));

		imgDropArea.setEnabled(false);
		imgDropArea.setBackground(new Color(126, 177, 255));
		imgDropArea.setDragEnabled(true);
		imgDropArea.setTransferHandler(new TransferHandler() {
			public boolean importData(JComponent c, Transferable t) {
				try {
					convertImg.setEnabled(false);

					Object object = t.getTransferData(DataFlavor.javaFileListFlavor);
					String tmp = object.toString();
					pathSelected = tmp.substring(1, tmp.length() - 1);
					FileOrFolder = false;
					checkIfIsImgThenEnableConvert();
					return true;
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			public boolean canImport(JComponent c, DataFlavor[] flavors) {
				return true;
			}
		});

		panelDrag.add(imgDropArea);
		panelDrag.setBorder(titleDrag);

		panelControl = new JPanel();
		Border etchedAfter = BorderFactory.createEtchedBorder();
		Border titleAfter = BorderFactory.createTitledBorder(etchedAfter, "其他控制选项");
		panelControl.setBorder(titleAfter);

		labelScale = new JLabel("缩放选项");
		labelScale.setFont(new Font("华文楷体", Font.PLAIN, 18));
		panelControl.add(labelScale);
		JComboBox<String> faceCombo = new JComboBox<>();
		faceCombo.addItem("10%");
		faceCombo.addItem("20%");
		faceCombo.addItem("25%");
		faceCombo.addItem("50%");
		faceCombo.addItem("不缩放");
		faceCombo.setSelectedIndex(4);
		faceCombo.addActionListener(event -> {
			int selectedIndex = faceCombo.getSelectedIndex();
			if (selectedIndex == 0) {
				scale = 0.1;
			} else if (selectedIndex == 1) {
				scale = 0.2;
			} else if (selectedIndex == 2) {
				scale = 0.25;
			} else if (selectedIndex == 3) {
				scale = 0.5;
			} else if (selectedIndex == 4) {
				scale = 1;
			}
		});

		panelControl.add(faceCombo);

		JLabel marginLabel = new JLabel("        ");
		panelControl.add(marginLabel);

		buttonHolderWindowsAbove = new JCheckBox("保持窗口最前");
		buttonHolderWindowsAbove.setFont(new Font("华文楷体", Font.PLAIN, 18));
		ActionListener buttonHolderWindowsAboveListener = event -> {
			if (buttonHolderWindowsAbove.isSelected()) {
				frame.setAlwaysOnTop(true);
			} else {
				frame.setAlwaysOnTop(false);
			}
		};
		buttonHolderWindowsAbove.addActionListener(buttonHolderWindowsAboveListener);
		panelControl.add(buttonHolderWindowsAbove);

		setLayout(new GridLayout(3, 1));
		container.add(panelBefore);
		container.add(panelDrag);
		container.add(panelControl);
		pack();
	}

	private class FileIconView extends FileView {
		private FileFilter filter;
		private Icon icon;

		public FileIconView(FileFilter aFilter, Icon anIcon) {
			filter = aFilter;
			icon = anIcon;
		}

		public Icon getIcon(File f) {
			if (!f.isDirectory() && filter.accept(f))
				return icon;
			else
				return null;
		}
	}

	private class ImagePreviewer extends JLabel {
		public ImagePreviewer(JFileChooser chooser) {
			setPreferredSize(new Dimension(100, 100));
			setBorder(BorderFactory.createEtchedBorder());

			chooser.addPropertyChangeListener(event -> {
				if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
					// the user has selected a new file
					File f = (File) event.getNewValue();
					if (f == null) {
						setIcon(null);
						return;
					}

					// read the image into an icon
					ImageIcon icon = new ImageIcon(f.getPath());

					// if the icon is too large to fit, scale it
					if (icon.getIconWidth() > getWidth())
						icon = new ImageIcon(icon.getImage().getScaledInstance(getWidth(), -1, Image.SCALE_DEFAULT));

					setIcon(icon);
				}
			});
		}
	}

	public void checkIfIsImgThenEnableConvert() {
		int index = pathSelected.lastIndexOf(".");
		String extensionName = pathSelected.substring(index + 1);
		if ("jpg".equalsIgnoreCase(extensionName) || "jpeg".equalsIgnoreCase(extensionName)
				|| "gif".equalsIgnoreCase(extensionName) || "png".equalsIgnoreCase(extensionName)
				|| "bmp".equalsIgnoreCase(extensionName)) {
			convertImg.setEnabled(true);
		}
	}

	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(() -> {
			frame = new MainWindows();
			frame.setTitle("Img2Ascii (By xiaoxi666)");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setSize(355, 255);
			frame.setResizable(false);
			frame.setAlwaysOnTop(true);
		});
	}
}

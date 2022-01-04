package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UtilDemo {
	static Alert alert = new Alert(AlertType.INFORMATION);//����information���͵ĶԻ���
	static List<File> listFiles = new ArrayList<>(); // �����ļ���·��

	// ��ʼ���赥����
	static public ListView<Label> initDate() {//�б�ؼ�
		UtilDemo.listFiles = UtilDemo.readSongMenuFiles();
		if (UtilDemo.listFiles != null) {
			Label[] listLabel = new Label[UtilDemo.listFiles.size()];
			AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//���赥�Ĵ�С��ֵ��maxItem(������Ŀ)
			ObservableList<Label> obLabel = FXCollections.observableArrayList();
			int i = 0;
			for (File lf : UtilDemo.listFiles) {//ѭ����ȡ�赥
				listLabel[i] = new Label(lf.getName());//��ȡ����������
				obLabel.add(listLabel[i]);
				i++;
			}
			LayoutDemo.lv = new ListView<Label>(obLabel);//�б�ؼ�
			return LayoutDemo.lv;
		} else {
			// UtilDemo.displayAlert();
			return null;
		}
	}

	/*
	 * �Ѹ赥����ļ�
	 * 
	 * @param �������ļ��б�
	 */
	static void saveSongMenuFiles(List<File> list) {
		Path path = Paths.get("songMenu_temp.txt");
		if (!Files.exists(path)) {//����ļ������ھʹ���
			try {
				Files.createFile(path.getFileName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (BufferedWriter bw = Files.newBufferedWriter(path)) {
			for (File f : list) {//ѭ��д���ļ���Ϣ
				bw.write(f.getPath());
				bw.newLine();//����
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * �Ѹ赥������
	 * 
	 * @return ����һ���赥�ļ��б�
	 */
	static List<File> readSongMenuFiles() {
		Path path = Paths.get("songMenu_temp.txt");
		List<File> list = new ArrayList<>();
		List<String> lStr = new ArrayList<>();
		if (Files.exists(path)) {
			try {
				lStr = Files.readAllLines(path);//��ȡÿ�е���Ϣ
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String str : lStr) {
				list.add(new File(str));
			}
			return list;//���ظ赥�ļ��б�
		}
		return null;
	}

	/*
	 * �ѷ��ϸ�ʽ�ĸ����ҳ�����mp3Ϊ����
	 * 
	 * @param �����Ǹ�����Ŀ¼
	 * 
	 * @return ���ظ�Ŀ¼�����з��������ĸ赥�ļ��б�
	 */
	static List<File> foundFiles(File file) {
		if (file != null) {
			List<File> list = new ArrayList<>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isFile()) {
					if (file2.getName().endsWith(".mp3")) {//�ҳ�mp3�ļ�
						list.add(file2);//�����б�
					}
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/*
	 * ��ʾû�и赥����ܰ��ʾ  information�Ի���
	 */
	static public void displayAlert() {
		alert.setTitle("������Ϣ");//���ñ���
		alert.setHeaderText("��ܰ����");//����Header
		alert.setContentText("����δѡ�������");//�����ı�����
		alert.show();//show�������ܿ���
	}
}

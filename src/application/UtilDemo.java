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
	static Alert alert = new Alert(AlertType.INFORMATION);//设置information类型的对话框
	static List<File> listFiles = new ArrayList<>(); // 歌曲文件的路径

	// 初始化歌单内容
	static public ListView<Label> initDate() {//列表控件
		UtilDemo.listFiles = UtilDemo.readSongMenuFiles();
		if (UtilDemo.listFiles != null) {
			Label[] listLabel = new Label[UtilDemo.listFiles.size()];
			AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//将歌单的大小赋值给maxItem(歌曲数目)
			ObservableList<Label> obLabel = FXCollections.observableArrayList();
			int i = 0;
			for (File lf : UtilDemo.listFiles) {//循环读取歌单
				listLabel[i] = new Label(lf.getName());//获取歌曲的名字
				obLabel.add(listLabel[i]);
				i++;
			}
			LayoutDemo.lv = new ListView<Label>(obLabel);//列表控件
			return LayoutDemo.lv;
		} else {
			// UtilDemo.displayAlert();
			return null;
		}
	}

	/*
	 * 把歌单存进文件
	 * 
	 * @param 参数是文件列表
	 */
	static void saveSongMenuFiles(List<File> list) {
		Path path = Paths.get("songMenu_temp.txt");
		if (!Files.exists(path)) {//如果文件不存在就创建
			try {
				Files.createFile(path.getFileName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (BufferedWriter bw = Files.newBufferedWriter(path)) {
			for (File f : list) {//循环写入文件信息
				bw.write(f.getPath());
				bw.newLine();//换行
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 把歌单读出来
	 * 
	 * @return 返回一个歌单文件列表
	 */
	static List<File> readSongMenuFiles() {
		Path path = Paths.get("songMenu_temp.txt");
		List<File> list = new ArrayList<>();
		List<String> lStr = new ArrayList<>();
		if (Files.exists(path)) {
			try {
				lStr = Files.readAllLines(path);//读取每行的信息
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String str : lStr) {
				list.add(new File(str));
			}
			return list;//返回歌单文件列表
		}
		return null;
	}

	/*
	 * 把符合格式的歌曲找出来（mp3为主）
	 * 
	 * @param 参数是歌曲根目录
	 * 
	 * @return 返回该目录的所有符合条件的歌单文件列表
	 */
	static List<File> foundFiles(File file) {
		if (file != null) {
			List<File> list = new ArrayList<>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isFile()) {
					if (file2.getName().endsWith(".mp3")) {//找出mp3文件
						list.add(file2);//加入列表
					}
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/*
	 * 显示没有歌单的温馨提示  information对话框
	 */
	static public void displayAlert() {
		alert.setTitle("提醒信息");//设置标题
		alert.setHeaderText("温馨提醒");//设置Header
		alert.setContentText("您尚未选择歌曲！");//设置文本内容
		alert.show();//show出来才能看到
	}
}

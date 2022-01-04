package application;

import java.io.File;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EventHandlerDemo {

	static public Stage getStage() {
		Stage primaryStage = new Stage();//定义舞台

		/********************* 歌曲列表的事件处理 ****************///有鼠标事件的注册
		LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {//匿名内部类写法
			@Override
			public void handle(MouseEvent event) {//重写handle方法
				// 鼠标双击
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					if (AudioPlayerDemo.mediaPlayer != null)
						AudioPlayerDemo.mediaPlayer.stop();//先让现在播放的歌曲停下
					
					if (AudioPlayerDemo.itemNum != -1) {
						LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BLACK);//将选中的标为黑色
					}
					AudioPlayerDemo.itemNum = LayoutDemo.lv.getSelectionModel().getSelectedIndex();
					LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BROWN);

					File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);
					String source = path.toURI().toString();//获得媒体资源的URL
					Media media = new Media(source);//临时新定义一个Medio
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//将Medio设置给medioPlayer
					LayoutDemo.bindSlider();//设置可拖动的进度条

					if (AudioPlayerDemo.itemNum < 11) {
						LayoutDemo.lv.scrollTo(0);
					} else {
						LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
					}

					LayoutDemo.doParse();//自定义显示歌名事件
					AudioPlayerDemo.mediaPlayer.play();//播放
					LayoutDemo.play.setText("暂停");//播放按钮上的文本转换为暂停
				}
			}
		});

		/********************* 菜单文件事件处理 ****************/
		// 打开歌曲文件事件处理
		LayoutDemo.openSongFiles.setOnAction(new EventHandler<ActionEvent>() {//匿名内部类写法
			@Override
			public void handle(ActionEvent event) {//重写handle方法
				FileChooser fileChooser = new FileChooser();//定义一个FileChooser 文件选择
				fileChooser.setTitle("添加歌曲");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("媒体文件", "*.mp3"));//添加mp3文件
				List<File> tempLv = null;
				tempLv = fileChooser.showOpenMultipleDialog(primaryStage);

				if (tempLv != null) {//选择了歌曲
					UtilDemo.listFiles = tempLv;
					Label[] listLabel = new Label[UtilDemo.listFiles.size()];
					AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//将选择歌曲的大小赋值为歌曲数目
					ObservableList<Label> obLabel = FXCollections.observableArrayList();
					int i = 0;
					for (File lf : UtilDemo.listFiles) {//循环读取选择的歌曲的名字
						listLabel[i] = new Label(lf.getName());
						obLabel.add(listLabel[i]);
						i++;
					}
					AudioPlayerDemo.itemNum = -1;
					LayoutDemo.lv = new ListView<Label>(obLabel);
					LayoutDemo.bp.setRight(LayoutDemo.lv);//页面控件的布局
					UtilDemo.saveSongMenuFiles(UtilDemo.listFiles);//将歌单存进文件
					//设置事件的响应
					LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							// 鼠标双击
							if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
								if (AudioPlayerDemo.mediaPlayer != null)
									AudioPlayerDemo.mediaPlayer.stop();//将现在播放的音乐停止
								AudioPlayerDemo.itemNum = LayoutDemo.lv.getSelectionModel().getSelectedIndex();
								LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BROWN);//设置选中的文本框的颜色

								File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);
								String source = path.toURI().toString();//获取要播放音乐的路径（URL）
								Media media = new Media(source);//临时定义一个新的Medio
								AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//将Medio添加给medioPlayer
								LayoutDemo.bindSlider();//重设进度条
								if (AudioPlayerDemo.itemNum < 11) {
									LayoutDemo.lv.scrollTo(0);
								} else {
									LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
								}
								LayoutDemo.doParse();//自定义显示歌名事件
								AudioPlayerDemo.mediaPlayer.play();//播放
								LayoutDemo.play.setText("暂停");
							}
						}
					});

				} else {
					UtilDemo.displayAlert();//否则没有选择歌曲，弹出提示对话框
					return;
				}
			}

		});

		// 打开目录的事件处理
		LayoutDemo.openDirectory.setOnAction(e -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();//定义一个DirectoryChooser
			directoryChooser.setTitle("选择歌曲目录");
			File dirFilePath = directoryChooser.showDialog(primaryStage);
			List<File> tempLv = null;//临时定义一个List
			tempLv = UtilDemo.foundFiles(dirFilePath);
			if (tempLv != null) {
				UtilDemo.listFiles = tempLv;//赋值
				Label[] listLabel = new Label[UtilDemo.listFiles.size()];
				AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//将文件中的歌曲的数目赋值给maxItem
				ObservableList<Label> obLabel = FXCollections.observableArrayList();
				int i = 0;
				for (File lf : UtilDemo.listFiles) {//循环读取文件歌名信息
					listLabel[i] = new Label(lf.getName());
					obLabel.add(listLabel[i]);
					i++;
				}
				AudioPlayerDemo.itemNum = -1;
				LayoutDemo.lv = new ListView<Label>(obLabel);//控件的布局
				LayoutDemo.bp.setRight(LayoutDemo.lv);
				UtilDemo.saveSongMenuFiles(UtilDemo.listFiles);
				
				LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// 鼠标双击  代码几乎同上述一样
						if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
							if (AudioPlayerDemo.mediaPlayer != null)
								AudioPlayerDemo.mediaPlayer.stop();

							AudioPlayerDemo.itemNum = LayoutDemo.lv.getSelectionModel().getSelectedIndex();
							LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BROWN);

							File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);
							// System.out.println(path);
							String source = path.toURI().toString();
							Media media = new Media(source);
							AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);
							LayoutDemo.bindSlider();
							if (AudioPlayerDemo.itemNum < 11) {
								LayoutDemo.lv.scrollTo(0);
							} else {
								LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
							}

							LayoutDemo.doParse();
							AudioPlayerDemo.mediaPlayer.play();
							LayoutDemo.play.setText("暂停");
						}
					}
				});

			} else {
				UtilDemo.displayAlert();
				return;
			}

		});

		// 关闭软件事件处理
		LayoutDemo.closeSoftware.setOnAction(e -> Platform.exit());

		/********************* 按钮事件处理 ****************/
		// 播放按钮事件处理
		LayoutDemo.play.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {
				if (UtilDemo.listFiles != null) {
					File path = UtilDemo.listFiles.get(++AudioPlayerDemo.itemNum);
					String source = path.toURI().toString();
					Media media = new Media(source);
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);
					LayoutDemo.bindSlider();
					LayoutDemo.doParse();
					AudioPlayerDemo.mediaPlayer.play();//播放
					LayoutDemo.play.setText("暂停");
				} else {
					UtilDemo.displayAlert();
					return;
				}
			} else {
				if (LayoutDemo.play.getText().equals("播放")) {
					AudioPlayerDemo.mediaPlayer.play();
					LayoutDemo.play.setText("暂停");
				} else {
					AudioPlayerDemo.mediaPlayer.pause();//暂停
					LayoutDemo.play.setText("播放");
				}
			}

		});

		// 上一曲事件处理
		LayoutDemo.last.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {//没有歌曲，弹出对话框提示
				UtilDemo.displayAlert();
				return;
			}
			if (AudioPlayerDemo.mediaPlayer != null)
				AudioPlayerDemo.mediaPlayer.stop();//先把现在播放的音乐暂停
			AudioPlayerDemo.itemNum--;//将要播放曲目的索引-1
			if (AudioPlayerDemo.itemNum < 0) {
				AudioPlayerDemo.itemNum = AudioPlayerDemo.maxItem - 1;
				//如果刚才播放的歌曲是播放列表中第一首，现在就播放播放列表中最后一首
			}
			File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
			String source = path.toURI().toString();//取得要播放歌曲的路径（URL）
			Media media = new Media(source);//临时定义一个Medio
			AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//将Medio加入medioPlayer
			LayoutDemo.bp.setCenter(LayoutDemo.mediaView);//将其布局在中间部分
			LayoutDemo.bindSlider();//重设进度条
			LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);
			if (AudioPlayerDemo.itemNum < 11) {
				LayoutDemo.lv.scrollTo(0);
			} else {
				LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
			}

			LayoutDemo.doParse();//自定义显示歌曲名字事件
			AudioPlayerDemo.mediaPlayer.play();//播放
			LayoutDemo.play.setText("暂停");
		});

		// 下一曲事件处理
		LayoutDemo.next.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {//没有歌曲，弹出对话框提示
				UtilDemo.displayAlert();
				return;
			}
			if (AudioPlayerDemo.mediaPlayer != null)
				AudioPlayerDemo.mediaPlayer.stop();//现将现在正在播放的歌曲暂停
			AudioPlayerDemo.itemNum++;//将要播放的曲目索引+1
			if (AudioPlayerDemo.itemNum >= AudioPlayerDemo.maxItem) {
				AudioPlayerDemo.itemNum = 0;//如果刚才播放的歌曲已经是播放列表中最后一首，那么现在就播放第一首
			}
			File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
			String source = path.toURI().toString();//取得要播放歌曲的路径（URL）
			Media media = new Media(source);//临时定义一个Medio
			AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//将Medio加入medioPlayer
			LayoutDemo.bp.setCenter(LayoutDemo.mediaView);//将其布局在中间部分
			LayoutDemo.bindSlider();//重设进度条
			LayoutDemo.doParse();//自定义显示歌曲名字事件

			LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);
			if (AudioPlayerDemo.itemNum < 11) {
				LayoutDemo.lv.scrollTo(0);
			} else {
				LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
			}
			AudioPlayerDemo.mediaPlayer.play();
			LayoutDemo.play.setText("暂停");
		});

		// 循环播放事件  主要看medioPlayer.setCycleCount(),括号中的内容 1就是单曲循环  MediaPlayer.INDEFINITE就是整个一起循环
		LayoutDemo.loop.setOnAction(e -> {
			if (AudioPlayerDemo.mediaPlayer != null && "单曲循环".equals(LayoutDemo.loop.getText())) {
				AudioPlayerDemo.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				LayoutDemo.loop.setText("循序播放");
			} else {
				AudioPlayerDemo.mediaPlayer.setCycleCount(1);
				LayoutDemo.loop.setText("单曲循环");
			}
		});

		// 停止播放的事件处理
		LayoutDemo.stop.setOnAction(e -> {
			if (AudioPlayerDemo.mediaPlayer != null) {
				AudioPlayerDemo.mediaPlayer.stop();//停止
				LayoutDemo.play.setText("播放");
			}
		});

		primaryStage.setScene(LayoutDemo.sc);
		primaryStage.setResizable(false);
		return primaryStage;
	}
}

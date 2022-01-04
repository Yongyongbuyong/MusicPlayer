package application;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class LayoutDemo {//布局子控件函数
	static MenuItem openSongFiles = new MenuItem("打开歌曲文件");//创建3个菜单的可选项
	static MenuItem openDirectory = new MenuItem("打开目录");
	static MenuItem closeSoftware = new MenuItem("关闭软件");
	static Button play = new Button("播放");//设置5个按钮
	static Button last = new Button("上一曲");
	static Button next = new Button("下一曲");
	static Button loop = new Button("单曲循环");
	static Button stop = new Button("停止");
	static Slider slider = new Slider();//可拖动进度条
	static Label timeLabel = new Label();//设置显示时间进度的文本
	static BorderPane bp = new BorderPane();//设置BorderPaner容器
	static ListView<Label> lv = new ListView<Label>();//设置Label（文本）参数的列表控件ListView
	static Scene sc = new Scene(LayoutDemo.getBorderPane(), 700, 700);//设置场景
	static MediaView mediaView = null;//设置MedioView
	static TextFlow tf = new TextFlow();//设置可自动换行的子文本控件

	// 显示歌名事件
	static public void doParse() {
		File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);//当前播放歌曲的路径
		String str = "正在播放：\n";
		str += path.getName();//获得歌名
		Text text = new Text(str);
		text.setFill(Color.MEDIUMVIOLETRED);//设置文本的颜色
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		tf.getChildren().clear();//先清空原先显示的
		tf.getChildren().add(text);//再把当前的文本添加进来
		bp.setCenter(tf);//将tf文本控件布局在中间
	}

	// 菜单
	static public MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();//定义菜单
		Menu fileMenu = new Menu("文件(_F)");
		fileMenu.setMnemonicParsing(true);
		menuBar.getMenus().add(fileMenu);//将fileMenu添加给menuBar
		fileMenu.getItems().addAll(openSongFiles, openDirectory, closeSoftware);//将之前定义的三个可选项加入菜单控件
		return menuBar;//最后返回得到一个菜单
	}

	// 滑动条的监听器
	static public void bindSlider() {
		slider.setMaxWidth(Region.USE_PREF_SIZE);//设置滑动条的最大宽度 （是一个常量）
		slider.setShowTickMarks(true);
		
		if (AudioPlayerDemo.mediaPlayer != null) {
			AudioPlayerDemo.mediaPlayer.currentTimeProperty().addListener(e -> {//添加监听者
				// 音频播放完继续播放
				if (AudioPlayerDemo.mediaPlayer.getCurrentTime().equals(AudioPlayerDemo.mediaPlayer.getStopTime())) {
					if (AudioPlayerDemo.itemNum != -1) {//有在播放的歌曲
						LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem)//当前播放歌曲索引取余于歌曲总数
								.setTextFill(Color.BLACK);//设置文本框的颜色
					}
					AudioPlayerDemo.itemNum++;//要播放下一首歌曲
					File path1 = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
					String source1 = path1.toURI().toString();//获取歌曲的路径（URL）
					Media media1 = new Media(source1);//新创建一个Medio
					AudioPlayerDemo.mediaPlayer.dispose();
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media1);
					LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);//设置文本框颜色
					LayoutDemo.bindSlider();
					AudioPlayerDemo.mediaPlayer.play();//播放歌曲
				}

				timeLabel.setText(Seconds2Str(AudioPlayerDemo.mediaPlayer.getCurrentTime().toSeconds()));//获取现在播放的时间并精确到秒
				slider.setValue(AudioPlayerDemo.mediaPlayer.getCurrentTime().toMillis()//到毫秒
						/ AudioPlayerDemo.mediaPlayer.getStopTime().toMillis() * 100);//获取歌曲的结束时间也就是歌曲的总长
			});

			slider.valueProperty().addListener(e -> {//添加监听者
				if (slider.isValueChanging()) {//更新时间的显示
					AudioPlayerDemo.mediaPlayer.seek(new Duration(
							AudioPlayerDemo.mediaPlayer.getStopTime().multiply(slider.getValue() / 100).toMillis()));
				}
			});
		}
	}

	static public FlowPane getHbox() {
		FlowPane fp = new FlowPane(5, 5);//定义一个流式布局容器
		fp.getChildren().addAll(timeLabel, slider, play, last, next, loop, stop);//将控件全部添加进容器中
		fp.setPadding(new Insets(10, 10, 10, 10));//设置控件之间的距离
		fp.setAlignment(Pos.CENTER);//设置对齐方式是居中对齐
		return fp;//返回流式布局容器
	}

	static public BorderPane getBorderPane() {
		bp.setBottom(getHbox());//将FlowPane容器布局在BorderPane容器bp的
		bp.setTop(getMenuBar());//将菜单MenuBar设置在顶部
		bp.setCenter(tf);//将子文本控件设置在
		if (UtilDemo.initDate() != null)
			bp.setRight(UtilDemo.initDate());//将列表布局在右边
		return bp;
	}
	//获取时间的正确显示
	static String Seconds2Str(Double seconds) {
		Integer count = seconds.intValue();
		count = count % 3600;
		Integer Minutes = count / 60;//获取当前分数
		count = count % 60;//获取当前秒数
		String str = Minutes.toString() + ":" + count.toString();//分数+秒数的一个显示
		return str;
	}
}

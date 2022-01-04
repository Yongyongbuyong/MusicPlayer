package application;

import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class AudioPlayerDemo extends Application {

	static MediaPlayer mediaPlayer = null;//初始化一个MedioPlayer
	static int itemNum = -1;	//当前歌曲的索引  先初始化为-1 表示没有歌曲
	static int maxItem;	//歌曲数目 
	//歌曲数目计数从0开始，最后一首歌曲的索引为maxItem-1
	
	@Override //重写start方法
	public void start(Stage primaryStage) throws Exception {
		primaryStage = EventHandlerDemo.getStage();
		primaryStage.setTitle("媒体播放器");//设置界面的标题
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}

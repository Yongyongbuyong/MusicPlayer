package application;

import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class AudioPlayerDemo extends Application {

	static MediaPlayer mediaPlayer = null;//��ʼ��һ��MedioPlayer
	static int itemNum = -1;	//��ǰ����������  �ȳ�ʼ��Ϊ-1 ��ʾû�и���
	static int maxItem;	//������Ŀ 
	//������Ŀ������0��ʼ�����һ�׸���������ΪmaxItem-1
	
	@Override //��дstart����
	public void start(Stage primaryStage) throws Exception {
		primaryStage = EventHandlerDemo.getStage();
		primaryStage.setTitle("ý�岥����");//���ý���ı���
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}

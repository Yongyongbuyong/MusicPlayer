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

public class LayoutDemo {//�����ӿؼ�����
	static MenuItem openSongFiles = new MenuItem("�򿪸����ļ�");//����3���˵��Ŀ�ѡ��
	static MenuItem openDirectory = new MenuItem("��Ŀ¼");
	static MenuItem closeSoftware = new MenuItem("�ر����");
	static Button play = new Button("����");//����5����ť
	static Button last = new Button("��һ��");
	static Button next = new Button("��һ��");
	static Button loop = new Button("����ѭ��");
	static Button stop = new Button("ֹͣ");
	static Slider slider = new Slider();//���϶�������
	static Label timeLabel = new Label();//������ʾʱ����ȵ��ı�
	static BorderPane bp = new BorderPane();//����BorderPaner����
	static ListView<Label> lv = new ListView<Label>();//����Label���ı����������б�ؼ�ListView
	static Scene sc = new Scene(LayoutDemo.getBorderPane(), 700, 700);//���ó���
	static MediaView mediaView = null;//����MedioView
	static TextFlow tf = new TextFlow();//���ÿ��Զ����е����ı��ؼ�

	// ��ʾ�����¼�
	static public void doParse() {
		File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);//��ǰ���Ÿ�����·��
		String str = "���ڲ��ţ�\n";
		str += path.getName();//��ø���
		Text text = new Text(str);
		text.setFill(Color.MEDIUMVIOLETRED);//�����ı�����ɫ
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		tf.getChildren().clear();//�����ԭ����ʾ��
		tf.getChildren().add(text);//�ٰѵ�ǰ���ı���ӽ���
		bp.setCenter(tf);//��tf�ı��ؼ��������м�
	}

	// �˵�
	static public MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();//����˵�
		Menu fileMenu = new Menu("�ļ�(_F)");
		fileMenu.setMnemonicParsing(true);
		menuBar.getMenus().add(fileMenu);//��fileMenu��Ӹ�menuBar
		fileMenu.getItems().addAll(openSongFiles, openDirectory, closeSoftware);//��֮ǰ�����������ѡ�����˵��ؼ�
		return menuBar;//��󷵻صõ�һ���˵�
	}

	// �������ļ�����
	static public void bindSlider() {
		slider.setMaxWidth(Region.USE_PREF_SIZE);//���û������������ ����һ��������
		slider.setShowTickMarks(true);
		
		if (AudioPlayerDemo.mediaPlayer != null) {
			AudioPlayerDemo.mediaPlayer.currentTimeProperty().addListener(e -> {//��Ӽ�����
				// ��Ƶ�������������
				if (AudioPlayerDemo.mediaPlayer.getCurrentTime().equals(AudioPlayerDemo.mediaPlayer.getStopTime())) {
					if (AudioPlayerDemo.itemNum != -1) {//���ڲ��ŵĸ���
						LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem)//��ǰ���Ÿ�������ȡ���ڸ�������
								.setTextFill(Color.BLACK);//�����ı������ɫ
					}
					AudioPlayerDemo.itemNum++;//Ҫ������һ�׸���
					File path1 = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
					String source1 = path1.toURI().toString();//��ȡ������·����URL��
					Media media1 = new Media(source1);//�´���һ��Medio
					AudioPlayerDemo.mediaPlayer.dispose();
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media1);
					LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);//�����ı�����ɫ
					LayoutDemo.bindSlider();
					AudioPlayerDemo.mediaPlayer.play();//���Ÿ���
				}

				timeLabel.setText(Seconds2Str(AudioPlayerDemo.mediaPlayer.getCurrentTime().toSeconds()));//��ȡ���ڲ��ŵ�ʱ�䲢��ȷ����
				slider.setValue(AudioPlayerDemo.mediaPlayer.getCurrentTime().toMillis()//������
						/ AudioPlayerDemo.mediaPlayer.getStopTime().toMillis() * 100);//��ȡ�����Ľ���ʱ��Ҳ���Ǹ������ܳ�
			});

			slider.valueProperty().addListener(e -> {//��Ӽ�����
				if (slider.isValueChanging()) {//����ʱ�����ʾ
					AudioPlayerDemo.mediaPlayer.seek(new Duration(
							AudioPlayerDemo.mediaPlayer.getStopTime().multiply(slider.getValue() / 100).toMillis()));
				}
			});
		}
	}

	static public FlowPane getHbox() {
		FlowPane fp = new FlowPane(5, 5);//����һ����ʽ��������
		fp.getChildren().addAll(timeLabel, slider, play, last, next, loop, stop);//���ؼ�ȫ����ӽ�������
		fp.setPadding(new Insets(10, 10, 10, 10));//���ÿؼ�֮��ľ���
		fp.setAlignment(Pos.CENTER);//���ö��뷽ʽ�Ǿ��ж���
		return fp;//������ʽ��������
	}

	static public BorderPane getBorderPane() {
		bp.setBottom(getHbox());//��FlowPane����������BorderPane����bp��
		bp.setTop(getMenuBar());//���˵�MenuBar�����ڶ���
		bp.setCenter(tf);//�����ı��ؼ�������
		if (UtilDemo.initDate() != null)
			bp.setRight(UtilDemo.initDate());//���б������ұ�
		return bp;
	}
	//��ȡʱ�����ȷ��ʾ
	static String Seconds2Str(Double seconds) {
		Integer count = seconds.intValue();
		count = count % 3600;
		Integer Minutes = count / 60;//��ȡ��ǰ����
		count = count % 60;//��ȡ��ǰ����
		String str = Minutes.toString() + ":" + count.toString();//����+������һ����ʾ
		return str;
	}
}

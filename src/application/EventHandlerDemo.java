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
		Stage primaryStage = new Stage();//������̨

		/********************* �����б���¼����� ****************///������¼���ע��
		LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {//�����ڲ���д��
			@Override
			public void handle(MouseEvent event) {//��дhandle����
				// ���˫��
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					if (AudioPlayerDemo.mediaPlayer != null)
						AudioPlayerDemo.mediaPlayer.stop();//�������ڲ��ŵĸ���ͣ��
					
					if (AudioPlayerDemo.itemNum != -1) {
						LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BLACK);//��ѡ�еı�Ϊ��ɫ
					}
					AudioPlayerDemo.itemNum = LayoutDemo.lv.getSelectionModel().getSelectedIndex();
					LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BROWN);

					File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);
					String source = path.toURI().toString();//���ý����Դ��URL
					Media media = new Media(source);//��ʱ�¶���һ��Medio
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//��Medio���ø�medioPlayer
					LayoutDemo.bindSlider();//���ÿ��϶��Ľ�����

					if (AudioPlayerDemo.itemNum < 11) {
						LayoutDemo.lv.scrollTo(0);
					} else {
						LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
					}

					LayoutDemo.doParse();//�Զ�����ʾ�����¼�
					AudioPlayerDemo.mediaPlayer.play();//����
					LayoutDemo.play.setText("��ͣ");//���Ű�ť�ϵ��ı�ת��Ϊ��ͣ
				}
			}
		});

		/********************* �˵��ļ��¼����� ****************/
		// �򿪸����ļ��¼�����
		LayoutDemo.openSongFiles.setOnAction(new EventHandler<ActionEvent>() {//�����ڲ���д��
			@Override
			public void handle(ActionEvent event) {//��дhandle����
				FileChooser fileChooser = new FileChooser();//����һ��FileChooser �ļ�ѡ��
				fileChooser.setTitle("��Ӹ���");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ý���ļ�", "*.mp3"));//���mp3�ļ�
				List<File> tempLv = null;
				tempLv = fileChooser.showOpenMultipleDialog(primaryStage);

				if (tempLv != null) {//ѡ���˸���
					UtilDemo.listFiles = tempLv;
					Label[] listLabel = new Label[UtilDemo.listFiles.size()];
					AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//��ѡ������Ĵ�С��ֵΪ������Ŀ
					ObservableList<Label> obLabel = FXCollections.observableArrayList();
					int i = 0;
					for (File lf : UtilDemo.listFiles) {//ѭ����ȡѡ��ĸ���������
						listLabel[i] = new Label(lf.getName());
						obLabel.add(listLabel[i]);
						i++;
					}
					AudioPlayerDemo.itemNum = -1;
					LayoutDemo.lv = new ListView<Label>(obLabel);
					LayoutDemo.bp.setRight(LayoutDemo.lv);//ҳ��ؼ��Ĳ���
					UtilDemo.saveSongMenuFiles(UtilDemo.listFiles);//���赥����ļ�
					//�����¼�����Ӧ
					LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							// ���˫��
							if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
								if (AudioPlayerDemo.mediaPlayer != null)
									AudioPlayerDemo.mediaPlayer.stop();//�����ڲ��ŵ�����ֹͣ
								AudioPlayerDemo.itemNum = LayoutDemo.lv.getSelectionModel().getSelectedIndex();
								LayoutDemo.lv.getSelectionModel().getSelectedItem().setTextFill(Color.BROWN);//����ѡ�е��ı������ɫ

								File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum);
								String source = path.toURI().toString();//��ȡҪ�������ֵ�·����URL��
								Media media = new Media(source);//��ʱ����һ���µ�Medio
								AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//��Medio��Ӹ�medioPlayer
								LayoutDemo.bindSlider();//���������
								if (AudioPlayerDemo.itemNum < 11) {
									LayoutDemo.lv.scrollTo(0);
								} else {
									LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
								}
								LayoutDemo.doParse();//�Զ�����ʾ�����¼�
								AudioPlayerDemo.mediaPlayer.play();//����
								LayoutDemo.play.setText("��ͣ");
							}
						}
					});

				} else {
					UtilDemo.displayAlert();//����û��ѡ�������������ʾ�Ի���
					return;
				}
			}

		});

		// ��Ŀ¼���¼�����
		LayoutDemo.openDirectory.setOnAction(e -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();//����һ��DirectoryChooser
			directoryChooser.setTitle("ѡ�����Ŀ¼");
			File dirFilePath = directoryChooser.showDialog(primaryStage);
			List<File> tempLv = null;//��ʱ����һ��List
			tempLv = UtilDemo.foundFiles(dirFilePath);
			if (tempLv != null) {
				UtilDemo.listFiles = tempLv;//��ֵ
				Label[] listLabel = new Label[UtilDemo.listFiles.size()];
				AudioPlayerDemo.maxItem = UtilDemo.listFiles.size();//���ļ��еĸ�������Ŀ��ֵ��maxItem
				ObservableList<Label> obLabel = FXCollections.observableArrayList();
				int i = 0;
				for (File lf : UtilDemo.listFiles) {//ѭ����ȡ�ļ�������Ϣ
					listLabel[i] = new Label(lf.getName());
					obLabel.add(listLabel[i]);
					i++;
				}
				AudioPlayerDemo.itemNum = -1;
				LayoutDemo.lv = new ListView<Label>(obLabel);//�ؼ��Ĳ���
				LayoutDemo.bp.setRight(LayoutDemo.lv);
				UtilDemo.saveSongMenuFiles(UtilDemo.listFiles);
				
				LayoutDemo.lv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// ���˫��  ���뼸��ͬ����һ��
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
							LayoutDemo.play.setText("��ͣ");
						}
					}
				});

			} else {
				UtilDemo.displayAlert();
				return;
			}

		});

		// �ر�����¼�����
		LayoutDemo.closeSoftware.setOnAction(e -> Platform.exit());

		/********************* ��ť�¼����� ****************/
		// ���Ű�ť�¼�����
		LayoutDemo.play.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {
				if (UtilDemo.listFiles != null) {
					File path = UtilDemo.listFiles.get(++AudioPlayerDemo.itemNum);
					String source = path.toURI().toString();
					Media media = new Media(source);
					AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);
					LayoutDemo.bindSlider();
					LayoutDemo.doParse();
					AudioPlayerDemo.mediaPlayer.play();//����
					LayoutDemo.play.setText("��ͣ");
				} else {
					UtilDemo.displayAlert();
					return;
				}
			} else {
				if (LayoutDemo.play.getText().equals("����")) {
					AudioPlayerDemo.mediaPlayer.play();
					LayoutDemo.play.setText("��ͣ");
				} else {
					AudioPlayerDemo.mediaPlayer.pause();//��ͣ
					LayoutDemo.play.setText("����");
				}
			}

		});

		// ��һ���¼�����
		LayoutDemo.last.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {//û�и����������Ի�����ʾ
				UtilDemo.displayAlert();
				return;
			}
			if (AudioPlayerDemo.mediaPlayer != null)
				AudioPlayerDemo.mediaPlayer.stop();//�Ȱ����ڲ��ŵ�������ͣ
			AudioPlayerDemo.itemNum--;//��Ҫ������Ŀ������-1
			if (AudioPlayerDemo.itemNum < 0) {
				AudioPlayerDemo.itemNum = AudioPlayerDemo.maxItem - 1;
				//����ղŲ��ŵĸ����ǲ����б��е�һ�ף����ھͲ��Ų����б������һ��
			}
			File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
			String source = path.toURI().toString();//ȡ��Ҫ���Ÿ�����·����URL��
			Media media = new Media(source);//��ʱ����һ��Medio
			AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//��Medio����medioPlayer
			LayoutDemo.bp.setCenter(LayoutDemo.mediaView);//���䲼�����м䲿��
			LayoutDemo.bindSlider();//���������
			LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);
			if (AudioPlayerDemo.itemNum < 11) {
				LayoutDemo.lv.scrollTo(0);
			} else {
				LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
			}

			LayoutDemo.doParse();//�Զ�����ʾ���������¼�
			AudioPlayerDemo.mediaPlayer.play();//����
			LayoutDemo.play.setText("��ͣ");
		});

		// ��һ���¼�����
		LayoutDemo.next.setOnAction(e -> {
			if (AudioPlayerDemo.itemNum == -1) {//û�и����������Ի�����ʾ
				UtilDemo.displayAlert();
				return;
			}
			if (AudioPlayerDemo.mediaPlayer != null)
				AudioPlayerDemo.mediaPlayer.stop();//�ֽ��������ڲ��ŵĸ�����ͣ
			AudioPlayerDemo.itemNum++;//��Ҫ���ŵ���Ŀ����+1
			if (AudioPlayerDemo.itemNum >= AudioPlayerDemo.maxItem) {
				AudioPlayerDemo.itemNum = 0;//����ղŲ��ŵĸ����Ѿ��ǲ����б������һ�ף���ô���ھͲ��ŵ�һ��
			}
			File path = UtilDemo.listFiles.get(AudioPlayerDemo.itemNum % AudioPlayerDemo.maxItem);
			String source = path.toURI().toString();//ȡ��Ҫ���Ÿ�����·����URL��
			Media media = new Media(source);//��ʱ����һ��Medio
			AudioPlayerDemo.mediaPlayer = new MediaPlayer(media);//��Medio����medioPlayer
			LayoutDemo.bp.setCenter(LayoutDemo.mediaView);//���䲼�����м䲿��
			LayoutDemo.bindSlider();//���������
			LayoutDemo.doParse();//�Զ�����ʾ���������¼�

			LayoutDemo.lv.getItems().get(AudioPlayerDemo.itemNum).setTextFill(Color.BROWN);
			if (AudioPlayerDemo.itemNum < 11) {
				LayoutDemo.lv.scrollTo(0);
			} else {
				LayoutDemo.lv.scrollTo(AudioPlayerDemo.itemNum - 11);
			}
			AudioPlayerDemo.mediaPlayer.play();
			LayoutDemo.play.setText("��ͣ");
		});

		// ѭ�������¼�  ��Ҫ��medioPlayer.setCycleCount(),�����е����� 1���ǵ���ѭ��  MediaPlayer.INDEFINITE��������һ��ѭ��
		LayoutDemo.loop.setOnAction(e -> {
			if (AudioPlayerDemo.mediaPlayer != null && "����ѭ��".equals(LayoutDemo.loop.getText())) {
				AudioPlayerDemo.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				LayoutDemo.loop.setText("ѭ�򲥷�");
			} else {
				AudioPlayerDemo.mediaPlayer.setCycleCount(1);
				LayoutDemo.loop.setText("����ѭ��");
			}
		});

		// ֹͣ���ŵ��¼�����
		LayoutDemo.stop.setOnAction(e -> {
			if (AudioPlayerDemo.mediaPlayer != null) {
				AudioPlayerDemo.mediaPlayer.stop();//ֹͣ
				LayoutDemo.play.setText("����");
			}
		});

		primaryStage.setScene(LayoutDemo.sc);
		primaryStage.setResizable(false);
		return primaryStage;
	}
}

package com.farm.scripts.autogold.screen;
/*
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.Settings;
import com.wet.wired.jsr.recorder.ScreenRecorderListener;

import java.io.*;

public class TradeRecorder implements ScreenRecorderListener {
   private boolean recording = false;
   private GameScreenRecorder screenRecorder;
   private OutputStream out;

   public void record(String name) {
      if (!this.recording) {
         this.recording = true;

         try {
            File dir = new File(Settings.BOT_DATA_PATH + "autogold_records" + File.separator);
            if (!dir.exists()) {
               dir.mkdirs();
            }

            File file = new File(Settings.BOT_DATA_PATH + "autogold_records" + File.separator, name + ".cap");
            this.out = new FileOutputStream(file);
            this.screenRecorder = new GameScreenRecorder(this.out, this);
            this.screenRecorder.bot = Bot.get();
            this.screenRecorder.startRecording();
         } catch (FileNotFoundException var4) {
            var4.printStackTrace();
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public void stop() {
      if (this.recording) {
         this.recording = false;

         try {
            this.out.close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }

      }
   }

   public void frameRecorded(boolean b) throws IOException {
   }

   public void recordingStopped() {
   }
}
*/
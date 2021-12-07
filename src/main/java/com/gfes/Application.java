package com.gfes;


import com.gfes.config.ApplicationContextConfig;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;

public class Application {

	public static void main(String[] args) {
		try
		{
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
		}
		catch(Exception e)
		{
			//TODO exception
		}
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
	}
}

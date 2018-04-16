/*
 * Copyright 2009-2011 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include "SerialPort.h"

#include "android/log.h"
static const char *TAG="serial_port";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

static speed_t getBaudrate(jint baudrate)
{
	switch(baudrate) {
	case 0: return B0;
	case 50: return B50;
	case 75: return B75;
	case 110: return B110;
	case 134: return B134;
	case 150: return B150;
	case 200: return B200;
	case 300: return B300;
	case 600: return B600;
	case 1200: return B1200;
	case 1800: return B1800;
	case 2400: return B2400;
	case 4800: return B4800;
	case 9600: return B9600;
	case 19200: return B19200;
	case 38400: return B38400;
	case 57600: return B57600;
	case 115200: return B115200;
	case 230400: return B230400;
	case 460800: return B460800;
	case 500000: return B500000;
	case 576000: return B576000;
	case 921600: return B921600;
	case 1000000: return B1000000;
	case 1152000: return B1152000;
	case 1500000: return B1500000;
	case 2000000: return B2000000;
	case 2500000: return B2500000;
	case 3000000: return B3000000;
	case 3500000: return B3500000;
	case 4000000: return B4000000;
	default: return -1;
	}
}

/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_android_serialport_api_SerialPort_open(JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint flags, jint databits, jint stopbits, jint parity,jint flowcontrol)
{
	int fd;
	speed_t speed;
	jobject mFileDescriptor;

	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			LOGE("Invalid baudrate");
			return NULL;
		}
	}

	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		LOGD("Opening serial port %s with flags 0x%x", path_utf, O_RDWR | flags);
		fd = open(path_utf, O_RDWR | flags);
		LOGD("open() fd = %d", fd);
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd == -1)
		{
			/* Throw an exception */
			LOGE("Cannot open port");
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Configure device */
	{
		struct termios cfg;
		LOGD("Configuring serial port");
		if (tcgetattr(fd, &cfg))
		{
			LOGE("tcgetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}

		cfg.c_cflag |= CLOCAL | CREAD;
		cfg.c_cflag &= ~CRTSCTS;
		cfg.c_cflag &= ~CSIZE;
		cfg.c_cflag |= CS8;
		cfg.c_cflag &= ~(PARENB|CSTOPB);
		cfg.c_iflag |= INPCK;
		cfg.c_iflag &= ~ (IXON | IXOFF | IXANY);
		cfg.c_lflag &= ~ (ICANON | ECHO | ECHOE | ISIG);
		cfg.c_iflag &= ~ (INLCR | ICRNL | IGNCR);
		cfg.c_oflag &= ~(ONLCR | OCRNL);
		cfmakeraw(&cfg);
		tcflush(fd, TCIOFLUSH);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);
		set_Parity(&cfg,databits,stopbits,parity,flowcontrol);

		if (tcsetattr(fd, TCSANOW, &cfg))
		{
			LOGE("tcsetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}
	}


	/* Create a corresponding file descriptor */
	{
		jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
		jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
		jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
		mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
		(*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint)fd);
	}


	return mFileDescriptor;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_android_serialport_api_SerialPort_close
  (JNIEnv *env, jobject thiz)
{
	jclass SerialPortClass = (*env)->GetObjectClass(env, thiz);
	jclass FileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");

	jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
	jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass, "descriptor", "I");

	jobject mFd = (*env)->GetObjectField(env, thiz, mFdID);
	jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);

	LOGD("close(fd = %d)", descriptor);
	close(descriptor);
}

/**
 *@brief   璁剧疆涓插彛鏁版嵁浣嶏紝鍋滄浣嶅拰鏁堥獙浣� *@param  fd     绫诲瀷  int  鎵撳紑鐨勪覆鍙ｆ枃浠跺彞鏌� *@param  databits 绫诲瀷  int 鏁版嵁浣�  鍙栧� 涓�7 鎴栬�8
 *@param  stopbits 绫诲瀷  int 鍋滄浣�  鍙栧�涓�1 鎴栬�2
 *@param  parity  绫诲瀷  int  鏁堥獙绫诲瀷 鍙栧�涓篘,E,O,,S
 */
int set_Parity(struct termios *options, int databits, int stopbits, int parity,int flowcontrol) {
	options->c_cflag &= ~CSIZE;
	switch (databits) /*璁剧疆鏁版嵁浣嶆暟*/
	{
	case 7:
		LOGD("options->c_cflag |= CS7");
		options->c_cflag |= CS7;
		break;
	case 8:
		LOGD("options->c_cflag |= CS8");
		options->c_cflag |= CS8;
		break;
	}

	switch (parity) {
	case 'n':
	case 'N':
		LOGD("options->c_cflag |=+n");
		options->c_cflag &= ~PARENB; /* Clear parity enable */
		options->c_iflag &= ~INPCK; /* Enable parity checking */
		break;
	case 'o':
	case 'O':
		LOGD("options->c_cflag |=+0");
		options->c_cflag |= (PARODD | PARENB); /* 璁剧疆涓哄鏁堥獙*/
		options->c_iflag |= INPCK; /* Disnable parity checking */
		break;
	case 'e':
	case 'E':
		LOGD("options->c_cflag |=+e");
		options->c_cflag |= PARENB; /* Enable parity */
		options->c_cflag &= ~PARODD; /* 杞崲涓哄伓鏁堥獙*/
		options->c_iflag |= INPCK; /* Disnable parity checking */
		break;
	case 'S':
	case 's': /*as no parity*/
		options->c_cflag &= ~PARENB;
		options->c_cflag &= ~CSTOPB;
		break;
	}
	switch (stopbits) {
	case 1:
		LOGD("options->c_cflag |=1");
		options->c_cflag &= ~CSTOPB;
		break;
	case 2:
		LOGD("options->c_cflag |=2");
		options->c_cflag |= CSTOPB;
		break;
	}

	switch(flowcontrol){
	case 0:
		LOGD("options->c_cflag &= ~CRTSCTS");
		options->c_cflag &= ~CRTSCTS;
		break;
	case 1:
		LOGD("options->c_cflag |= CRTSCTS");
		options->c_cflag |= CRTSCTS;
		break;
	case 2:
		LOGD("options->c_cflag | = IXON|IXOFF|IXANY");
		options->c_cflag |= IXON|IXOFF|IXANY;
		break;
	}


}

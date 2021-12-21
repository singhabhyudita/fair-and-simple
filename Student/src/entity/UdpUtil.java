package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpUtil {

    public static void sendObjectToPort(Object o, int port) {
        try {
            byte [] ob = objectToByteArray(o);
            DatagramPacket dp = new DatagramPacket(ob, ob.length, InetAddress.getLocalHost(), port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getObjectFromPort(DatagramSocket ds) {
        try {
            byte [] ob = new byte[65500];
            DatagramPacket dp = new DatagramPacket(ob, ob.length);
            ds.receive(dp);
            return byteArrayToObject(ob);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] objectToByteArray(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bufferedImageToByteArray(BufferedImage capturedImage) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(capturedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static Object byteArrayToObject(byte [] ba) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage byteArrayToBufferedImage(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

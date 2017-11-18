package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageTagManager {
    private static ImageTagManager instance = null;
    private HashMap<String, ArrayList<Image>> allTags;
    private HashMap<String, Image> allImages;

    private ImageTagManager() {
        // Exists only to defeat instantiation.
    }

    public static ImageTagManager getInstance() {
        if (instance == null) {
            instance = new ImageTagManager();
            instance.allTags = new HashMap<>();
            instance.allImages = new HashMap<>();
        }
        return instance;
    }

    void add(Tag tag) {
        ArrayList<Image> list;
        if (allTags.containsKey(tag.getName())) {
            list = allTags.get(tag.getName());
            list.add(tag.getImage());
        } else {
            list = new ArrayList<>();
            list.add(tag.getImage());
            allTags.put(tag.getName(), list);
        }
        System.out.println(allTags);
    }

    void addImage(Image image){
        allImages.put(image.getPath().toString(), image);
        System.out.println(allImages);
    }

    void delete(String tagName, Image image) {
        allTags.get(tagName).remove(image);
    }

    public void saveToFile() throws IOException {

        OutputStream file = new FileOutputStream("images.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        OutputStream file1 = new FileOutputStream("tags.ser");
        OutputStream buffer1 = new BufferedOutputStream(file1);
        ObjectOutput output1 = new ObjectOutputStream(buffer1);

        // serialize the Map
        output1.writeObject(allTags);
        output.writeObject(allImages);
        output.close();
        output1.close();
    }

    public void readFromFile() throws IOException, ClassNotFoundException {

            InputStream file = new FileInputStream("images.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            InputStream file1 = new FileInputStream("tags.ser");
            InputStream buffer1 = new BufferedInputStream(file1);
            ObjectInput input1 = new ObjectInputStream(buffer1);

            //deserialize the Map
            Object a  = input.readObject();
            Object b = input1.readObject();
            allImages = (HashMap<String,Image>) a;
            allTags = (HashMap<String, ArrayList<Image>>) b;
            input.close();
           input1.close();
        System.out.println(allImages);
        System.out.println(allTags);

    }

}



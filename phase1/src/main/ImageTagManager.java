package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

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

    public String[] getListOfTags() {
        Set<String> setOfTagString = allTags.keySet();
        String[] listOfTags = setOfTagString.toArray(new
                String[setOfTagString.size()]);
        Arrays.sort(listOfTags);
        return listOfTags;
    }

    public HashMap<String, Image> getAllImages() {
        return allImages;
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

    void addImage(Image image) {
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

    public void readFromFile() throws IOException {

        InputStream file = null;
        try {
            file = new FileInputStream("images.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            InputStream file1 = new FileInputStream("tags.ser");
            InputStream buffer1 = new BufferedInputStream(file1);
            ObjectInput input1 = new ObjectInputStream(buffer1);
            Object a = null;
            Object b = null;
            try {
                a = input.readObject();
                b = input1.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Stopped");
            }
            allImages = (HashMap<String, Image>) a;
            allTags = (HashMap<String, ArrayList<Image>>) b;

            input.close();
            input1.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        //deserialize the Map
//
        System.out.println(allImages);
        System.out.println(allTags);

    }

}



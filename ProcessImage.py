# Code adapted from Tensorflow Object Detection Framework
# https://github.com/tensorflow/models/blob/master/research/object_detection/object_detection_tutorial.ipynb
# Tensorflow Object Detection Detector

import numpy as np
import tensorflow as tf
import cv2
import time
import os, os.path
import pyrebase
import argparse
import os.path
import socket
import matplotlib.pyplot as plt



class DetectorAPI:
    def __init__(self, path_to_ckpt):
        self.path_to_ckpt = path_to_ckpt

        self.detection_graph = tf.Graph()
        with self.detection_graph.as_default():
            od_graph_def = tf.GraphDef()
            with tf.gfile.GFile(self.path_to_ckpt, 'rb') as fid:
                serialized_graph = fid.read()
                od_graph_def.ParseFromString(serialized_graph)
                tf.import_graph_def(od_graph_def, name='')

        self.default_graph = self.detection_graph.as_default()
        self.sess = tf.Session(graph=self.detection_graph)

        # Definite input and output Tensors for detection_graph
        self.image_tensor = self.detection_graph.get_tensor_by_name('image_tensor:0')
        # Each box represents a part of the image where a particular object was detected.
        self.detection_boxes = self.detection_graph.get_tensor_by_name('detection_boxes:0')
        # Each score represent how level of confidence for each of the objects.
        # Score is shown on the result image, together with the class label.
        self.detection_scores = self.detection_graph.get_tensor_by_name('detection_scores:0')
        self.detection_classes = self.detection_graph.get_tensor_by_name('detection_classes:0')
        self.num_detections = self.detection_graph.get_tensor_by_name('num_detections:0')

    def processFrame(self, image):
        # Expand dimensions since the trained_model expects images to have shape: [1, None, None, 3]
        image_np_expanded = np.expand_dims(image, axis=0)
        # Actual detection.
        start_time = time.time()
        (boxes, scores, classes, num) = self.sess.run(
            [self.detection_boxes, self.detection_scores, self.detection_classes, self.num_detections],
            feed_dict={self.image_tensor: image_np_expanded})
        end_time = time.time()

       # print("Elapsed Time:", end_time-start_time)

        im_height, im_width,_ = image.shape
        boxes_list = [None for i in range(boxes.shape[1])]
        for i in range(boxes.shape[1]):
            boxes_list[i] = (int(boxes[0,i,0] * im_height),
                        int(boxes[0,i,1]*im_width),
                        int(boxes[0,i,2] * im_height),
                        int(boxes[0,i,3]*im_width))

        return boxes_list, scores[0].tolist(), [int(x) for x in classes[0].tolist()], int(num[0])

    def close(self):
        self.sess.close()
        self.default_graph.close()

def GetColor(img):
    image = img
    boundaries = [([160, 175, 220], [210, 225, 270]), #pink
        ([17, 15, 100], [50, 56, 200]), #red
       ([17, 65, 150], [50, 110, 250]), #orange
       ([40, 185, 230], [90, 235, 280]),#yellow
       ([45, 80, 0], [95, 140, 45]),#green
       ([220, 200, 170], [270, 250, 220]), #light_blue
        ([86, 31, 4], [220, 88, 50]), #dark_blue
       ([150, 100, 100], [200, 160, 160]), #purple
       ([50, 50, 75], [100, 100, 120]), #brown
        ([150, 150, 150], [200, 200, 200]), #grey
        ([0, 0, 0],[50, 50, 50]), #black
        ([200,200,200],[255,255,255]) #white

    ]

    #background removal
    color_options=['pink','red','orange','yellow','green','light_blue','dark_blue','purple','brown','grey','black', 'white']
    color_pred=[]

    # loop over the boundaries
    for (lower, upper) in boundaries:
        lower = np.array(lower, dtype = "uint8")
        upper = np.array(upper, dtype = "uint8")

        # find the colors within the specified boundaries and apply
        # the mask
        mask = cv2.inRange(image, lower, upper)
        output = cv2.bitwise_and(image, image, mask = mask)
        #show the images
        #cv2.imshow("images", np.hstack([image, output]))
        #cv2.waitKey(0)
        color_pred.append(np.count_nonzero(mask))

    sum_colors=sum(color_pred)
    index_max=color_pred.index(max(color_pred))
    true_color= color_options[index_max]

    percentage_primary=color_pred[index_max]/sum_colors


    secondary_color_arr=color_pred
    secondary_color_arr[index_max]=0;
    second_index_max=color_pred.index(max(secondary_color_arr))
    secondary_color= color_options[second_index_max]


    percentage_secondary=color_pred[second_index_max]/sum_colors
    print('detected colors: ')
    print(true_color, percentage_primary)
    print(secondary_color, percentage_secondary)
    return (true_color, secondary_color)

def ProcessImage(imagename, of_id, folder):
    id = imagename
    imagename = id.split('/')[1]
    outfit_id = of_id
    db_path = folder
    print(id)
    model_path = 'models/faster_rcnn_inception_v2_coco_2018_01_28/frozen_inference_graph.pb'
    torso_model_path = 'torso_inference_graph/frozen_inference_graph.pb'
    odapi = DetectorAPI(path_to_ckpt=model_path)
    torsoAPI = DetectorAPI(path_to_ckpt=torso_model_path)
    threshold = 0.8
    #img_name = input('which image?\n')
    #img_path = 'test_images/' + img_name +  '.jpg'1
    processed_img_path = 'processed_images/'
    #cap = cv2.VideoCapture(0)
    #face_cascade = cv2.CascadeClassifier('models/haarcascade_frontalface_default.sml')
    #lower_cascade = cv2.CascadeClassifier('models/haarcascade_lowerbody.xml')

    print('success.')
    print('reading image...')
    #img = cv2.imread(os.path.join(temp_path,id))
    img = cv2.imread(id)
    print('processing frame...')
    boxes, scores, classes, num = odapi.processFrame(img)
    for i in range(len(boxes)):
    # Class 1 represents human
        if classes[i] == 1 and scores[i] > threshold:
            box = boxes[i]
    cropped_img = img[box[0]:box[2],box[1]:box[3]]


    boxes, scores, classes, num = torsoAPI.processFrame(cropped_img)
    print('detecting body parts...')
    for i in range(len(boxes)):
    # Class 1 represents human
        if classes[i] == 1 and scores[i] > threshold:
            box = boxes[i]
    print('creating top')
    #create top half
    top_img = cropped_img[box[0]:box[2],box[1]:box[3]]
    print('creating bottom')
    #create bottom half
    bottom_img = cropped_img[box[2]:max(cropped_img.shape), box[1]:box[3]]


    #bottom_img = cropped_img[]
    print('writing top...')
    cv2.imwrite(processed_img_path + 'top/' + imagename + '_processed_top.jpg', top_img)
    print('success!')
    print('writing bottom...')
    cv2.imwrite(processed_img_path + 'bottom/' + imagename + '_processed_bot.jpg', bottom_img)
    print('success!')

    print('processing color')
    '''
    Do kishan's processing
    Remove background
    calculate 2 top colors, 2 bottom colors
    '''
    print('completed.')
    top_colors = GetColor(top_img)
    bottom_colors = GetColor(bottom_img)
    tc1 = top_colors[0]
    tc2 = top_colors[1]
    bc1 = bottom_colors[0]
    bc2 = bottom_colors[1]

    print(tc1, tc2, bc1, bc2)
    print('Updating database with outfit information.')
    db.child('outfits').child(outfit_id).update({"top_color_1": tc1,"top_color_2": tc2, "bot_color_1": bc1,"bot_color_2": bc2})

    print('Uploading top image to storage...')
    storage.child(folder).child('top.jpeg').put(processed_img_path + 'top/' + imagename + '_processed_top.jpg')
    print('success!')
    print('Uploading bottom image to storage...')
    storage.child(folder).child('bottom.jpeg').put(processed_img_path + 'bottom/' + imagename + '_processed_bot.jpg')
    print('success!')

    print('image processing completed.')

    #print('Processed image ' + filename)

    #r, img = cap.read()

        # Visualization of the results of a detectionself.
#ProcessImage('image_20181111_162909.jpeg')

def stream_handler(message):
    print(message["event"]) # put
    print(message["path"]) # /-K7yGTTEp7O549EzTYtI
    print(message["data"]) # {'title': 'Pyrebase', "body": "etc..."}
    temp_path = 'temp/'
    if message["event"] == "patch":
        print(message)
        for key,value in message['data'].items():
            keys = key.split('/')
            values = value.split('#')
            outfit_id = keys[2]
            path = keys[0] + '/' + 'outfits/' + outfit_id + '/' + values[0] + '#' + outfit_id + '#' + values[1]
            folder = keys[0] + '/' + 'outfits/' + outfit_id
            print('downloading ' + path)
            storage.child(path).download(temp_path+value)
            print('success')
            ProcessImage(temp_path+value, outfit_id, folder)





ap = argparse.ArgumentParser()
ap.add_argument("-p", "--port", help = "port to run on")
args = vars(ap.parse_args())
port = args["port"]



#HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
#PORT = int(port)        # Port to listen on (non-privileged ports are > 1023)



config = {
    "apiKey"            :       "",
    "authDomain"        :       "dressme-appdatabase.firebaseapp.com",
    "databaseURL"       :       "https://dressme-appdatabase.firebaseio.com",
    "projectId"         :       "dressme-appdatabase",
    "storageBucket"     :       "dressme-appdatabase.appspot.com"
}

firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
user = auth.sign_in_with_email_and_password("tventura0297@gmail.com", "123456")
storage = firebase.storage()
db = firebase.database()

my_stream = db.child("users").stream(stream_handler, stream_id="new_posts")

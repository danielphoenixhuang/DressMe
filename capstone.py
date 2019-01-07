# -*- coding: utf-8 -*-
import numpy as np
import argparse
import cv2
import matplotlib.pyplot as plt

ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", help = "path to the image")
args = vars(ap.parse_args())
image = cv2.imread(args["image"])

#image = cv2.imread('blue.png')
boundaries = [([160, 175, 220], [210, 225, 270]), #pink
	([17, 15, 100], [50, 56, 200]), #red
   ([17, 65, 150], [50, 110, 250]), #orange 
   ([40, 185, 230], [90, 235, 280]),#yellow
   ([45, 80, 0], [95, 140, 45]),#green
   ([220, 200, 170], [270, 250, 220]), #light_blue
	([86, 31, 4], [220, 88, 50]), #dark_blue
   ([60, 45, 100], [110, 95, 150]), #purple
   ([50, 50, 75], [100, 100, 120]), #brown
	([150, 150, 150], [200, 200, 200]) #grey
    
]

#background removal
mask = np.zeros(image.shape[:2], np.uint8)

bgdModel = np.zeros((1,65),np.float64)
fgdModel = np.zeros((1,65),np.float64)
rows,cols,ch= image.shape
rect = (0,0,rows,cols)
cv2.grabCut(image, mask, rect, bgdModel, fgdModel, 5, cv2.GC_INIT_WITH_RECT)
mask2=np.where((mask==2)|(mask==0),0,1).astype('uint8')
image=image*mask2[:,:,np.newaxis]
#cv2.imshow("clothe",image)
#cv2.waitKey(0)

#brown=image[413,23,:]
#biege=image[195,23,:]
#green=image[268,23,:]
#light_blue=image[175,23,:]


color_options=['pink','red','orange','yellow','green','light_blue','dark_blue','purple','brown','grey']
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

index_max=color_pred.index(max(color_pred))
true_color= color_options[index_max]

secondary_color_arr=color_pred
secondary_color_arr[index_max]=0;
second_index_max=color_pred.index(max(secondary_color_arr))
secondary_color= color_options[second_index_max]

print("Primary color:", true_color)
print("Secondary color:",secondary_color)
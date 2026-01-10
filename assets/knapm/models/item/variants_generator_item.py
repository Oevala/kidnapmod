import os
import sys

colors = ["orange", "red", "white", "yellow", "brown", 
          "magenta", "purple", "blue", "light_blue", "green", "lime", 
          "pink", "cyan", "gray", "silver", "black"]



def generate_color_variants(fileName, name):
    with open(fileName, "r") as f:
        extracted_infos = os.path.splitext(fileName)
        realName = extracted_infos[0]
        extension = extracted_infos[1]
        lines = f.readlines()
        for c in colors:
            newName = realName + "_" + c
            newF = open(newName + extension, "w")
            for line in lines:
                newline = replace_last(line, name, name + "_" + c)
                newF.write(newline)
            newF.close()

def replace_last(s, old, new):
    return (s[::-1].replace(old[::-1],new[::-1], 1))[::-1]

def main():
    if len(sys.argv) == 3:
        generate_color_variants(sys.argv[1], sys.argv[2])
    else :
        print("Error")
        
main()
    
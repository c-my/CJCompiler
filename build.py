import os
import shutil


proj_dir = os.getcwd()
src_dir = os.path.join(proj_dir, "src")
lib_dir = os.path.join(proj_dir, "lib")
out_dir = os.path.join(proj_dir, "out")
res_dir = os.path.join(proj_dir, "res")
jar = os.path.join(proj_dir, "CJCompiler.jar")
MANIFEST = os.path.join(proj_dir, "MANIFEST.MF")

src_files = []
for path, dir_names, file_names in os.walk(src_dir):
    file_paths = [os.path.join(path, x) for x in file_names]
    src_files += file_paths

lib_files = [os.path.join(lib_dir, x) for x in os.listdir(lib_dir)]

shutil.rmtree(out_dir, True)
cmd = f"javac -d {out_dir} -encoding utf-8 -cp .;{';'.join(lib_files)} -sourcepath {src_dir} {' '.join(src_files)}"
print(cmd)
os.system(cmd)

shutil.copytree(res_dir, os.path.join(out_dir, "res"))
shutil.copytree(lib_dir, os.path.join(out_dir, "lib"))


cmd = f"cd {out_dir} && jar -cvfm {jar} {MANIFEST} *"
print(cmd)
os.system(cmd)
import os

machine = "91"
machine = "163"
machine = "local"


ROOT = "/data1/jyy/style/evaluation"
if machine == "local":
    ROOT = r"C:\Users\dell\jyy\research\evaluation"

FORSEE_DATASET_DIR = os.path.join(ROOT, "src/lib/Forsee/dataset")
FORSEE_CACHES = os.path.join(ROOT, "src/lib/Forsee/caches")

DATA_DIR = os.path.join(ROOT, "data")
EVAL_DIR = os.path.join(ROOT, "evaluation")




TMP_DATA = os.path.join(ROOT, "tmp-data")
META_DATA_DIR = os.path.join(ROOT, "meta-data")
FORSEE_EVAL_RESULT_DIR = os.path.join(TMP_DATA, "forsee", "classify")
RESULT_DIR = os.path.join(TMP_DATA, "forsee", "classify")
TEST_DIR=os.path.join(ROOT, "test")

MODELS_DIR = '/data/jyy/style/models'
if machine == "163":
    MODELS_DIR = '/data1/jyy/models'
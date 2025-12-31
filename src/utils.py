import os

from path import *

DATASET_CREATION = "across-project"
# DATASET_CREATION = ""

def create_src_codes_dir(project_name):
    return os.path.join(TMP_DATA, project_name, "src-codes")

def create_target_codes_dir(project_name):
    return os.path.join(TMP_DATA, project_name, "target-codes")

def create_codes_dir(project_name):
    return os.path.join(DATA_DIR, project_name, "codes")

def create_test_result_path(project_name):
    return os.path.join(TEST_DIR, f"{project_name}-test.jsonl")

# Output to forsee evaluation directory
def create_result_codes_dir(project_name):
    return os.path.join(DATA_DIR, project_name, f"result-codes")

def create_transformation_pair_path(project_name, min_target_code_lines):
    filename = f"{project_name}-pairs.jsonl" if project_name else "pairs.jsonl" 
    return os.path.join(DATA_DIR, DATASET_CREATION, str(min_target_code_lines), filename)

def create_tested_data_csv_path(project_name):
    return os.path.join(DATA_DIR, project_name, f"data-with-tests.csv")

def create_transformation_result_jsonl_path(method, min_target_code_lines):
    return os.path.join(DATA_DIR, DATASET_CREATION, str(min_target_code_lines), f"{method}-result.jsonl")


def create_no_tests_data_csv_path(project_name):
    return os.path.join(DATA_DIR, project_name, f"data-no-tests.csv")

def create_forsee_eval_path(project_name):
    return os.path.join(FORSEE_EVAL_RESULT_DIR, f"{project_name}-eval-result.jsonl")

def create_forsee_train_codes_dir(project_name):
    return os.path.join(FORSEE_DATASET_DIR, project_name)

def create_forsee_eval_codes_dir(project_name):
    return os.path.join(FORSEE_DATASET_DIR, f"{project_name}-eval")


def create_forsee_bin_classification_stats_path(project_name):
    return os.path.join(FORSEE_EVAL_RESULT_DIR, "csv", f"{project_name}-acc-stats.csv")

def create_forsee_bin_classification_acc_path(project_name):
    return os.path.join(FORSEE_EVAL_RESULT_DIR, "csv", f"{project_name}-acc-matrix.csv")

def create_project_classify_reuslts_path(project_name):
    return os.path.join(DATA_DIR, DATASET_CREATION, "classify_result", f"{project_name}-classify-result.jsonl")

###################### Meta Data  ######################

def create_raw_data_meta_csv_path(project_name):
    return os.path.join(META_DATA_DIR, 'csv', f"{project_name}-raw-data.csv")

def create_pairs_meta_path(project_name, min_target_code_lines):
    filename = f"{project_name}-pairs-meta.csv" if project_name else "pairs-meta.csv"
    return os.path.join(META_DATA_DIR, DATASET_CREATION, str(min_target_code_lines), filename) 


###################### Evaluation Data  ######################
def create_eval_transform_types_path(min_target_lines):
    return os.path.join(EVAL_DIR,DATASET_CREATION, str(min_target_lines), "transform-types-table.csv")

def create_eval_transform_success_rate_path(min_target_lines):
    return os.path.join(EVAL_DIR,DATASET_CREATION, str(min_target_lines), "success-rate-table.csv")

def create_eval_results_dir(min_target_lines):
    return os.path.join(EVAL_DIR,DATASET_CREATION, str(min_target_lines))

def create_eval_results_path(method, min_target_lines):
    return os.path.join(EVAL_DIR, DATASET_CREATION, str(min_target_lines), f"{method}-eval-results.jsonl")


def create_transformed_classify_results_path(method, min_target_lines):
    return os.path.join(EVAL_DIR, DATASET_CREATION, str(min_target_lines), f"{method}-transformed-classify.jsonl")

def create_author_id_map_path():
    return os.path.join(DATA_DIR, "author-id-map.jsonl")
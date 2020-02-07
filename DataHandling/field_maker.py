import pandas as pd

data_path = "./data/precautions_data/"

final_df = pd.read_csv("./data/최종파일.csv", encoding="cp949")
DUR_age_ban_list = pd.read_csv(data_path + "DUR특정연령대금기정보목록.csv", encoding="cp949")
old_cau_list = pd.read_csv(data_path + "노인주의품목.csv", encoding="cp949")
div_cau = pd.read_excel(data_path + "분할주의.xlsx", encoding="cp949")
volume_cau = pd.read_excel(data_path + "용량주의.xlsx", encoding="cp949")
pregnant_ban = pd.read_excel(data_path + "임부금기.xlsx", encoding="cp949")
term_cau = pd.read_csv(data_path + "투여기간주의.csv", encoding="utf-8")
dup_cau = pd.read_excel(data_path + "효능군중복주의.xlsx", encoding="cp949")


def extract_num_and_content(df, cau_name):
    if "품목일련번호" in df.columns:
        df = df[["금기내용", "품목일련번호"]]
        df.columns = [cau_name, "품목기준코드"]
    elif "품목기준코드" in df.columns:
        df = df[["금기내용", "품목기준코드"]]
        df.columns = [cau_name, "품목기준코드"]
    return df


new_columns = ["특정연령금기", "노인주의", "분할주의", "임부금기", "용량주의", "투여기간주의", "효능군중복주의"]
df_list = [DUR_age_ban_list, old_cau_list, div_cau, pregnant_ban, volume_cau, term_cau, dup_cau]

for i in range(len(df_list)):
    df_list[i] = extract_num_and_content(df_list[i], new_columns[i])

for i in range(len(df_list)):
    final_df = pd.merge(final_df, df_list[i], on="품목기준코드", how="left")


final_df.head()


# 각 부작용 내용들이 잘 들어갔는지 확인.
valid = []

for i in range(len(df_list)):
    if len(df_list[i][new_columns[i]].value_counts()) == len(final_df[new_columns[i]].value_counts()):
        valid.append(True)
    else:
        valid.append(False)
valid


# save df

final_df.to_csv("./data/부작용_추가_최종파일.csv", encoding="cp949", index=False)
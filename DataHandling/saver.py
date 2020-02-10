import pymysql
import pandas as pd
import sys

df = pd.read_excel("final_rev02.xlsx", encoding="cp949")


def df_to_mysql(host, user, password, db_table):

    try:
        conn = pymysql.connect(host=host,
                               user=user, password=password)

        print("Connected to DB: {}".format(""))

        cursor = conn.cursor()
        df_columns = df.columns
        print(df_columns)
        print(df.shape[0])

        for i in range(df.shape[0]):
            sql = "insert into {} values(".format(db_table) + "%s," * 14 + "%s)"
            cols = tuple([df[col][i] if type(df[col][i]) == str else "None" for col in df_columns])
            cursor.execute(sql, cols)
            conn.commit()

        print("Successfully save the table from file")
        conn.close()

    except Exception as e:
        print("Error: {}".format(str(e)))
        sys.exit(1)

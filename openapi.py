from lxml import etree  #xml 파일을 열 때 사용하는 코드
import xml.etree.ElementTree as ElementTree  #xml파일을 여는 코드
import pandas as pd

from urllib.request import urlopen
from urllib.parse import urlencode,unquote,quote_plus
import urllib

# 의약품 상세 정보
a = []
b = []
c = []
d = []
e = []
f = []
g = []
h = []
i = []
j = []
k = []
l = []
ll = []
m = []
n = []
o = []
p = []
q = []
r = []
s = []
t = []
u = []
v = []
x = []

for name in range(1, 509):

    # api주소 열기 (url + 인증키 + 해당페이지+한 화면에 출력되는 데이터 개수)
    request = urllib.request.Request(
        'http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?serviceKey=70chvGVOwguGdjPrpZL063fX50H6oufjCGWQ77SQ2AL0mOHl9nzn58ipR1dB5G0rjNmt7Xhj%2FZ3pPAXRBOq5Ig%3D%3D&pageNo='
        + str(name) + '&numOfRows=100')

    request.get_method = lambda: 'GET'
    response_body = urlopen(request).read()

    # 추출된 xml형식의 text를 xml객체로 파싱
    tree = etree.fromstring(response_body)

    # 태그로부터 원하는 텍스트 추출
    for media in tree.getiterator('item'):
        a.append(media.findtext('ITEM_SEQ'))
        b.append(media.findtext('ITEM_NAME'))
        c.append(media.findtext('ENTP_NAME'))
        d.append(media.findtext('ITEM_PERMIT_DATE'))
        e.append(media.findtext('ETC_OTC_CODE'))
        f.append(media.findtext('CLASS_NO'))
        g.append(media.findtext('CHART'))
        h.append(media.findtext('BAR_CODE'))
        i.append(media.findtext('MATERIAL_NAME'))
        j.append(media.findtext('EE_DOC_ID'))
        k.append(media.findtext('UD_DOC_ID'))
        l.append(media.findtext('NB_DOC_ID'))
        ll.append(media.findtext('INSERT_FILE'))
        m.append(media.findtext('STORAGE_METHOD'))
        n.append(media.findtext('VALID_TERM'))
        o.append(media.findtext('REEXAM_TARGET'))
        p.append(media.findtext('PACK_UNIT'))
        q.append(media.findtext('EDI_CODE'))
        r.append(media.findtext('PERMIT_KIND_NAME'))
        s.append(media.findtext('ENTP_NO'))
        t.append(media.findtext('MAKE_MATERIAL_FLAG'))
        u.append(media.findtext('INDUSTY_TYPE'))
        v.append(media.findtext('CANCEL_NAME'))
        x.append(media.findtext('NARCOTIC_KIND_CODE'))

    # 데이터프레임으로 변환 후 저장        
df1 = pd.DataFrame(a, columns=['품목기준코드'])
df2 = pd.DataFrame(b, columns=['품목명'])
df3 = pd.DataFrame(c, columns=['업체명'])
df4 = pd.DataFrame(d, columns=['허가일자'])
df5 = pd.DataFrame(e, columns=['전문일반'])
df6 = pd.DataFrame(f, columns=['분류'])
df7 = pd.DataFrame(g, columns=['성상'])
df8 = pd.DataFrame(h, columns=['표준코드'])
df9 = pd.DataFrame(i, columns=['원료성분'])
df10 = pd.DataFrame(j, columns=['효능효과'])
df11 = pd.DataFrame(k, columns=['용법용량'])
df12 = pd.DataFrame(l, columns=['주의사항'])
df12_2 = pd.DataFrame(ll, columns=['첨부문서'])
df13 = pd.DataFrame(m, columns=['저장방법'])
df14 = pd.DataFrame(n, columns=['유효기간'])
df15 = pd.DataFrame(o, columns=['재심사대상'])
df16 = pd.DataFrame(p, columns=['포장단위'])
df17 = pd.DataFrame(q, columns=['보험코드'])
df18 = pd.DataFrame(r, columns=['허가/신고구분'])
df19 = pd.DataFrame(s, columns=['업체허가번호'])
df20 = pd.DataFrame(t, columns=['완제/원료구분'])
df21 = pd.DataFrame(u, columns=['업종구분'])
df22 = pd.DataFrame(v, columns=['상태'])
df23 = pd.DataFrame(x, columns=['마약종류코드'])

medical_total = pd.concat(
    [df1, df2, df3, df4, df5, df6, df7, df8, df10, df11, df12, df13, df14, df15, df16, df17, df18, df19, df20, df21,
     df22, df23], axis=1)
medical_total.to_csv('의약품 상세조회(openapi).csv', encoding='cp949')

# 의약품 주성분
a = []
b = []
c = []
d = []
e = []
f = []
g = []
h = []
i = []
j = []

for name in range(1, 522):

    # api주소 열기 (url + 인증키 + 해당페이지+한 화면에 출력되는 데이터 개수)
    request = urllib.request.Request(
        'http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductList?serviceKey=70chvGVOwguGdjPrpZL063fX50H6oufjCGWQ77SQ2AL0mOHl9nzn58ipR1dB5G0rjNmt7Xhj%2FZ3pPAXRBOq5Ig%3D%3D&pageNo='
        + str(name) + '&numOfRows=100')

    request.get_method = lambda: 'GET'
    response_body = urlopen(request).read()

    # 추출된 xml형식의 text를 xml객체로 파싱
    tree = etree.fromstring(response_body)

    # 태그로부터 원하는 텍스트 추출
    for media in tree.getiterator('item'):
        a.append(media.findtext('ITEM_SEQ'))
        b.append(media.findtext('ITEM_NAME'))
        c.append(media.findtext('ENTP_NAME'))
        d.append(media.findtext('ITEM_PERMIT_DATE'))
        e.append(media.findtext('INDUTY'))
        f.append(media.findtext('PRDLST_STDR_CODE'))
        g.append(media.findtext('PRDUCT_PRMISN_NO'))
        h.append(media.findtext('ITEM_INGR_NAME'))
        i.append(media.findtext('ITEM_INGR_CNT'))
        j.append(media.findtext('PRODUCT_TYPE'))

# 데이터프레임으로 변환 후 저장
df1 = pd.DataFrame(a, columns=['품목기준코드'])
df2 = pd.DataFrame(b, columns=['품목명'])
df3 = pd.DataFrame(c, columns=['업체명'])
df4 = pd.DataFrame(d, columns=['품목허가일자'])
df5 = pd.DataFrame(e, columns=['업종'])
df6 = pd.DataFrame(f, columns=['품목일련번호'])
df7 = pd.DataFrame(g, columns=['품목허가번호'])
df8 = pd.DataFrame(h, columns=['주성분'])
df9 = pd.DataFrame(i, columns=['주성분수'])
df10 = pd.DataFrame(j, columns=['분류명'])

medical_main = pd.concat([df1, df2, df3, df4, df5, df6, df7, df8, df9, df10], axis=1)
medical_main.to_csv('의약품 주성분(openapi).csv', encoding='cp949')

total = pd.merge(medical_total,medical_main,on=['품목기준코드'])
total.to_csv('의약품 상세정보_rev01.csv',encoding='cp949')
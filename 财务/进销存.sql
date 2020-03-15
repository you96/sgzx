SELECT
	(
		SELECT
			NAME
		FROM
			e_base_goodssupplier a
		WHERE
			a.id = SUPPLIER_ID
	) AS '供应商名称',
	(
		SELECT
			NAME
		FROM
			e_base_goodscategory a
		WHERE
			a.SORT = CATEGORY_ID
	) AS '品种名称',
	GOODS_NAME,
	LEFT (GOODS_CODE,8) AS GOOD_CODE,
	AFTERSALESSUPPORT_PRICE,
	TAX_RATE,
	NOTAX_PRICE,
	ifnull(
		sum(CASE WHEN flang = 0 THEN q3 END),
		0
	) AS '中心仓期初数量',
	ifnull(
		sum(CASE WHEN flang = 0 THEN s3 END),
		0
	) AS '中心仓仓期初金额',
	ifnull(
		sum(CASE WHEN flang = 4 THEN q3 END),
		0
	) AS '销售入库数量',
	ifnull(
		sum(CASE WHEN flang = 4 THEN s3 END),
		0
	) AS '销售入库金额',
	ifnull(
		sum(CASE WHEN flang = 6 THEN q3 END),
		0
	) AS '不合格品入库数量',
	ifnull(
		sum(CASE WHEN flang = 6 THEN s3 END),
		0
	) AS '不合格品入库金额',
		ifnull(
		sum(CASE WHEN flang = 8 THEN q3 END),
		0
	) AS '报残出库数量',
	ifnull(
		sum(CASE WHEN flang = 8 THEN s3 END),
		0
	) AS '报残出库金额',
	
	ifnull(
		sum(CASE WHEN flang = 9 THEN q3 END),
		0
	) AS '销售出库数量',
	ifnull(
		sum(CASE WHEN flang = 9 THEN s3 END),
		0
	) AS '销售出库金额',
	ifnull(
		sum(CASE WHEN flang = 10 THEN q3 END),
		0
	) AS '退库数量',
	ifnull(
		sum(CASE WHEN flang = 10 THEN s3 END),
		0
	) AS '退库数量金额',

	ifnull(
		sum(CASE WHEN flang = 12 THEN q3 END),
		0
	) AS '销售退库数量',
	ifnull(
		sum(CASE WHEN flang = 12 THEN s3 END),
		0
	) AS '销售退库金额',
	ifnull(
		sum(CASE WHEN flang = 13 THEN q3 END),
		0
	) AS '不合格品跨月处置数量',
	ifnull(
		sum(CASE WHEN flang = 13 THEN s3 END),
		0
	) AS '不合格品跨月处置金额',
	ifnull(
		sum(CASE WHEN flang = 14 THEN q3 END),
		0
	) AS '退货跨月处置数量',
	ifnull(
		sum(CASE WHEN flang = 14 THEN s3 END),
		0
	) AS '退货跨月处置金额'
FROM
	(
		(
			SELECT
				a.SUPPER_ID AS SUPPLIER_ID,
				a.CATEGORY_ID,
				a.GOODS_NAME,
				a.GOODS_CODE,
				a.AFTERSALESSUPPORT_PRICE,
				a.TAX_RATE,
				a.NOTAX_PRICE,
				SUM(a.QUANTITY) AS q3,
				SUM(a.NOTAX_PRICE * a.QUANTITY) AS s3,
				0 AS flang
			FROM
				e_stock_stockmonthremains a,
				e_base_goodsinfo c
			WHERE
				a.goods_id = c.id
			AND c.CUSTOMIZED = '1'
			AND a.DATA_MONTH = '202001'
			AND a.STOCK_TYPE = '1'
			GROUP BY
				a.GOODS_CODE,
				TAX_RATE,
				NOTAX_PRICE
		)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					4 AS flang
				FROM
					e_bill_centerinventory a,
					e_bill_centerinventoryinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '4'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					6 AS flang
				FROM
					e_bill_centerinventory a,
					e_bill_centerinventoryinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '10'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					8 AS flang
				FROM
					e_bill_centeroutstorage a,
					e_bill_centeroutstorageinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '3'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					9 AS flang
				FROM
					e_bill_centeroutstorage a,
					e_bill_centeroutstorageinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '0'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					10 AS flang
				FROM
					e_bill_outstorage a,
					e_bill_outstorageinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '1'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					b.SUPPLIER_ID,
					b.CATEGORY_ID,
					b.GOODS_NAME,
					b.GOODS_CODE,
					b.AFTERSALESSUPPORT_PRICE,
					b.TAX_RATE,
					b.NOTAX_PRICE,
					SUM(b.QUANTITY) AS q3,
					SUM(b.NOTAX_PRICE * b.QUANTITY) AS s3,
					12 AS flang
				FROM
					e_bill_centerinventory a,
					e_bill_centerinventoryinfo b,
					e_base_goodsinfo g
				WHERE
					a.BILL_NO = b.BILL_NO
				AND b.GOODS_ID = g.ID
				AND g.CUSTOMIZED = '1'
				AND a.BILL_DATE >= '${begin_date}'
				AND a.BILL_DATE < '${end_date}'
				AND a.CLASSIFY = '5'
				GROUP BY
					b.GOODS_CODE,
					b.TAX_RATE,
					b.NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					a.SUPPLIER_ID AS SUPPLIER_ID,
					a.CATEGORY_ID,
					a.GOODS_NAME,
					a.GOODS_CODE,
					a.AFTERSALESSUPPORT_PRICE,
					a.TAX_RATE,
					a.NOTAX_PRICE,
					SUM(a.QUANTITY) AS q3,
					SUM(a.NOTAX_PRICE * a.QUANTITY) AS s3,
					13 AS flang
				FROM
					e_bill_nonconforminginfo a,
					e_bill_nonconforming n,
					e_base_goodsinfo g
				WHERE
					a.bill_no = n.bill_no
				AND a.goods_id = g.id
				AND g.CUSTOMIZED = '1'
				AND n.BILL_DATE < '${end_date}'
				AND (
					(
						n.Need_Financeaudit = 1
						AND n.FINANCEAUDIT_DATE >= '${begin_date}'
						AND n.FINANCEAUDIT_DATE < '${end_date}'
					)
					OR (
						n.Need_Financeaudit = 0
						AND STORAGEAUDIT_DATE >= '${begin_date}'
						AND STORAGEAUDIT_DATE < '${end_date}'
					)
				)
				GROUP BY
					a.GOODS_CODE,
					a.TAX_RATE,
					NOTAX_PRICE
			)
		UNION ALL
			(
				SELECT
					a.SUPPLIER_ID AS SUPPLIER_ID,
					a.CATEGORY_ID,
					a.GOODS_NAME,
					a.GOODS_CODE,
					a.AFTERSALESSUPPORT_PRICE,
					a.TAX_RATE,
					a.NOTAX_PRICE,
					SUM(a.QUANTITY) AS q3,
					SUM(a.NOTAX_PRICE * a.QUANTITY) AS s3,
					14 AS flang
				FROM
					e_bill_returngoodsinfo a,
					e_bill_returngoods n,
					e_base_goodsinfo g
				WHERE
					a.bill_no = n.bill_no
				AND a.goods_id = g.id
				AND g.CUSTOMIZED = '1'
				AND n.BILL_DATE < '${end_date}'
				AND (
					(
						n.Need_Financeaudit = 1
						AND n.FINANCEAUDIT_DATE >= '${begin_date}'
						AND n.FINANCEAUDIT_DATE < '${end_date}'
					)
					OR (
						n.Need_Financeaudit = 0
						AND STORAGEAUDIT_DATE >= '${begin_date}'
						AND STORAGEAUDIT_DATE < '${end_date}'
					)
				)
				GROUP BY
					a.GOODS_CODE,
					a.TAX_RATE,
					NOTAX_PRICE
			)
	) xyz
GROUP BY
	SUPPLIER_ID,
	CATEGORY_ID,
	GOODS_NAME,
	LEFT (GOODS_CODE, 8),
	TAX_RATE,
	NOTAX_PRICE
ORDER BY
	GOODS_NAME,
	SUPPLIER_ID,
	CATEGORY_ID,
	AFTERSALESSUPPORT_PRICE,
	TAX_RATE
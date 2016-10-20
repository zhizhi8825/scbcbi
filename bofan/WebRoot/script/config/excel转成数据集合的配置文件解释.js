{
	"classMsg":{  //类型信息字段，里面说明结果返回的对象名称跟它对应的类型，这将在翻译数据跟结果返回中起到重要作用
		"billList":"com.gzwanhong.domain.Bill",
		"billDetailList":"com.gzwanhong.domain.BillDetail"
	},
	"startRow":1, //说明从excel的哪行开始读取数据
	"emptyClass":{ //判断是否为空的对象，比如这里，就是读每一行的数据时，如果第0列为空，则billList这个对象不取那一行的数据
		"billList":0
	},
	"flagCol":{ //标识列，该列相同的所有billList只有一条
		"billList":0
	},
	"columnMsg":[ //每一列的信息
	    {
	    	"columnName":"billCode", //列名
	    	"objectName":"billList", //说明该列的值将会保存在billList这个对象集合里
	    	"columnIndex":0, //说明该字段对应到excel中的第几列
	    	"methodName":"setBillCode", //说明该字段在billList对象类型里对应的set方法
	    	"argClass":"java.lang.String", //set方法的参数类型
	    	"defaultVal":""   //默认值
	    },
	    {
	    	"objectName":"billList",
	    	"columnIndex":1,
	    	"methodName":"setBillNumber",
	    	"argClass":"java.lang.Integer"
	    },
	    {
	    	"objectName":"billList",
	    	"columnIndex":2,
	    	"methodName":"setMoneyLower",
	    	"argClass":"java.lang.Double"
	    },
	    {
	    	"objectName":"billList",
	    	"columnIndex":3,
	    	"methodName":"setCreateDate",
	    	"argClass":"java.util.Date"
	    },
	    {
	    	"objectName":"billList",
	    	"columnIndex":5,
	    	"methodName":"setClassification",
	    	"argClass":"java.lang.Integer",
	    	"valMap":{     //如果该参数不为空的话，那就会把取到的值，再通过该valMap转换值
	    		"邮政普遍服务":51
	    	}
	    },
	    {
	    	"objectName":"billDetailList",
	    	"columnIndex":0,
	    	"methodName":"setBillCode",
	    	"argClass":"java.lang.String",
	    	"preRow":true   //如果该单元格的值为空的话，而这个参数又为true的话，那就向上面的行找该列的值，一直往上，直到找到值或到尽头为止
	    },
	    {
	    	"objectName":"billDetailList",
	    	"columnIndex":4,
	    	"methodName":"setInfo",
	    	"argClass":"java.lang.String"
	    },
	    {
	    	"objectName":"billDetailList",
	    	"columnIndex":5,
	    	"methodName":"setPrice",
	    	"argClass":"java.lang.Double"
	    }
	]
}
import 'package:flutter/material.dart';

//引入插件
import 'package:charts_flutter/flutter.dart' as charts;

class ChartsDemo extends StatefulWidget {
  _TestState createState() => _TestState();
}

class _TestState extends State<ChartsDemo> {

  String _year;
  int _sales;

  //点击柱状图触发的函数
  _onSelectionChanged(charts.SelectionModel model) {
    final selectedDatum = model.selectedDatum;
    print(selectedDatum.first.datum.year);
    print(selectedDatum.first.datum.sales);
    print(selectedDatum.first.series.displayName);
    setState(() {
      //改变两个显示的数值
      _year = selectedDatum.first.datum.year;
      _sales = selectedDatum.first.datum.sales;
    });
  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: AppBar(
          title: Text("图表"),
          centerTitle: true,
        ),
        body: Container(
          child: Column(
            children: <Widget>[
              Row(
                children: <Widget>[
                  Expanded(
                    child: Container(
                      alignment: Alignment.center,
                      child: Text("横轴：年份"),
                    ),
                  ),
                  Expanded(
                    child: Container(
                      alignment: Alignment.center,
                      child: Text("竖轴：数值"),
                    ),
                  )
                ],
              ),
              Container(
                width: double.infinity,
                height: 200.0,
                child: charts.BarChart(
                  //通过下面获取数据传入
                  ChartFlutterBean.createSampleData(),
                  //配置项，以及设置触发的函数
                  selectionModels: [
                    charts.SelectionModelConfig(
                      type: charts.SelectionModelType.info,
                      changedListener: _onSelectionChanged,
                    )
                  ],
                ),
              ),
            ],
          ),
        )
    );
  }


}
//一下为组合柱状图数据部分
class OrdinalSales {
  final String year;
  final int sales;

  OrdinalSales(this.year, this.sales);
}


class ChartFlutterBean {

  static List<charts.Series<OrdinalSales, String>> createSampleData() {
    final data = [
      new OrdinalSales('2020', 5),
      new OrdinalSales('2019', 25),
      new OrdinalSales('2018', 100),
      new OrdinalSales('2017', 75),
      new OrdinalSales('2016', 85),
      new OrdinalSales('2015', 50),
    ];

    return [
      new charts.Series<OrdinalSales, String>(
        id: 'Sales',
        colorFn: (_, __) => charts.MaterialPalette.blue.shadeDefault,
        domainFn: (OrdinalSales sales, _) => sales.year,
        measureFn: (OrdinalSales sales, _) => sales.sales,
        data: data,
      )
    ];
  }
}
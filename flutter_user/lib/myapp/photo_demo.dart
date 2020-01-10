
import 'package:flutter/material.dart';
import 'package:flutter_user/myapp/photo/photo2.dart';
import 'package:flutter_user/myapp/photo/photo_album.dart';

class PhotoDemo extends StatefulWidget {

  @override
  _PhotoState createState() {
    return _PhotoState();
  }
}

class _PhotoState extends State<PhotoDemo> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('拍照上传'),
      ),
      body: Container(
          child: Column(
            children: <Widget>[
              RaisedButton(
                  child: Text('拍照'),
                  onPressed: () {
                    Navigator.push(
                        context, MaterialPageRoute(builder: (context) {
                      return ImagePickerPage();
                    }));
                  }
              ),
              RaisedButton(
                  child: Text('选择本地照片'),
                  onPressed: () {
                    Navigator.push(
                        context, MaterialPageRoute(builder: (context) {
                      return PhotoAlbum();
                    }));
                  }
              ),
            ],
          )
      )
      ,
    );
  }
}
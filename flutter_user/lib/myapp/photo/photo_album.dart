import 'dart:io';

import 'package:flutter/material.dart';
import 'package:photo_album_manager/photo_album_manager.dart';

class PhotoAlbum extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return PhotoAlbumPage();
  }
}

class PhotoAlbumPage extends StatefulWidget {
  @override
  PhotoAlbumPageState createState() {
    return PhotoAlbumPageState();
  }
}

class PhotoAlbumPageState extends State<PhotoAlbumPage> {

  List<ImageInfo> imageList;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('选择照片'),
          actions: <Widget>[
//            Icon(Icons.save),
//            RaisedButton(
//                child: Text('确认选择'),
//                onPressed: () {}
//            ),
            Container(
                margin: EdgeInsets.all(10.0),
                width: 80.0,
                height: 20.0,
                alignment: Alignment.center,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.all(Radius.circular(10.0)),
                ),
                padding: EdgeInsets.all(0.0),
                child: Text(
                  '保存', style: TextStyle(fontSize: 16.0, color: Colors.blue),)
            )
          ],
        ),
        body: _getBody()
    );
  }

  Widget _getBody() {
    if (imageList == null || imageList.isEmpty) {
      Future.delayed(Duration(milliseconds: 250), () {
        _getPhoto();
      });
      return Container();
    }
    return Container(
        margin: EdgeInsets.all(4.0),
        child:
        GridView.builder(
          gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3,
            mainAxisSpacing: 4.0,
            crossAxisSpacing: 4.0,
          ),
          itemBuilder: (_, index) {
            return gridViewItem(imageList[index]);
          },
          itemCount: imageList.length,
        ));
  }

  Widget gridViewItem(ImageInfo imageInfo) {
    double w = MediaQuery
        .of(context)
        .size
        .width / 3 - 6;
    print("bool value=${imageInfo.check}");
    return Stack(
      alignment: Alignment.topRight,
      children: <Widget>[
        Container(
          color: Colors.black54,
          width: w,
          height: 130.0,
          child: Image.file(File(imageInfo.thumbPath), fit: BoxFit.cover),
        ),
        Container(
          width: 28.0,
          height: 28.0,
          alignment: Alignment.topRight,
          child: Checkbox(
              value: imageInfo.check,
              activeColor: Colors.green,
              onChanged: (bl) {
                setState(() {
                  imageInfo.check = !imageInfo.check;
                });
              }),
        )
      ],
    );
  }

  void _getPhoto() async {
    List<AlbumModelEntity> descAlbumList = await PhotoAlbumManager
        .getDescAlbumImg();
    if (descAlbumList != null && descAlbumList.isNotEmpty) {
      print("descAlbum size=${descAlbumList.length}");
      List<ImageInfo> list = descAlbumList.map((AlbumModelEntity en) {
        ImageInfo info = ImageInfo(check: false);
        info.creationDate = en.creationDate;
        info.localIdentifier = en.localIdentifier;
        info.originalPath = en.originalPath;
        info.resourceSize = en.resourceSize;
        info.resourceType = en.resourceType;
        info.thumbPath = en.thumbPath;
        return info;
      }).toList();
      print("ImageInfo list size=${list.length}");
      if (!mounted) return;
      setState(() {
        imageList = list;
      });
    }
  }
}

class ImageInfo extends AlbumModelEntity {

  bool check;

  ImageInfo({this.check});
}
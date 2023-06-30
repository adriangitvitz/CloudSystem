package com.example.springapi.util;

import com.example.springapi.models.dto.Listobjectsresponse;
import io.minio.messages.Item;

public class Itemparser {
    public static Listobjectsresponse parsebucketobjectitems(Item item) {
        Listobjectsresponse listobjectsresponse = new Listobjectsresponse();
        listobjectsresponse.setName(item.objectName());
        listobjectsresponse.setLastmodified(item.lastModified());
        listobjectsresponse.setIsdir(item.isDir());
        listobjectsresponse.setMetadata(item.userMetadata());
        return listobjectsresponse;
    }
}

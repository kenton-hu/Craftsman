package com.ktn.craftsman.service;
  
import com.ktn.craftsman.service.ICallback;  
  
interface IService {  
 void registerCallback(ICallback cb);  
 void unregisterCallback(ICallback cb);  
}  
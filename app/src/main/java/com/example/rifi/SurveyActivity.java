package com.example.rifi;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

public class SurveyActivity extends AppCompatActivity {

    int id;
    DatabaseHelper dbHelper;

    CheckBox e,g,w,s;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_survey);

        id=getIntent().getIntExtra("id",-1);
        dbHelper=new DatabaseHelper(this);

        e=findViewById(R.id.elec);
        g=findViewById(R.id.gas);
        w=findViewById(R.id.water);
        s=findViewById(R.id.sanit);
        rg=findViewById(R.id.status);

        findViewById(R.id.save).setOnClickListener(v->save());
        findViewById(R.id.camera).setOnClickListener(v->camera());
        findViewById(R.id.export).setOnClickListener(v->exportExcel());
    }

    void camera(){
        try{
            File file=new File(getExternalFilesDir(null),"img_"+id+".jpg");

            Uri uri= FileProvider.getUriForFile(this,
                    getPackageName()+".provider",
                    file);

            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(i,100);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void save(){
        String st="";
        int sel=rg.getCheckedRadioButtonId();

        if(sel==R.id.st1) st="في طور الانجاز";
        else if(sel==R.id.st2) st="على مستوى الاعمدة";
        else if(sel==R.id.st3) st="منتهية غير مشغولة";
        else if(sel==R.id.st4) st="منتهية ومشغولة";

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues v=new ContentValues();
        v.put("elec",e.isChecked()?1:0);
        v.put("gas",g.isChecked()?1:0);
        v.put("water",w.isChecked()?1:0);
        v.put("sanit",s.isChecked()?1:0);
        v.put("status",st);
        v.put("completed",1);

        db.update("beneficiaries",v,"id=?",new String[]{id+""});

        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        finish();
    }

    void exportExcel(){
        try{
            Workbook wb=new XSSFWorkbook();
            Sheet sheet=wb.createSheet("data");

            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor c=db.rawQuery("SELECT name,address,status FROM beneficiaries WHERE completed=1",null);

            int i=0;
            while(c.moveToNext()){
                Row r=sheet.createRow(i++);
                r.createCell(0).setCellValue(c.getString(0));
                r.createCell(1).setCellValue(c.getString(1));
                r.createCell(2).setCellValue(c.getString(2));
            }

            File file=new File(getExternalFilesDir(null),"report.xlsx");
            FileOutputStream out=new FileOutputStream(file);
            wb.write(out);
            out.close();

            Toast.makeText(this,"Excel created",Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

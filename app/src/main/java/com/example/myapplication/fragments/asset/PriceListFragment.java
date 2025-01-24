package com.example.myapplication.fragments.asset;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.pdf.PdfDocument;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PriceListAdapter;
import com.example.myapplication.domain.PriceListItem;
import com.example.myapplication.services.PriceListService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PriceListAdapter adapter;
    private List<PriceListItem> priceList = new ArrayList<>();
    private PriceListService priceListService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pricelist, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PriceListAdapter(priceList, priceListService);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btn_export_pdf).setOnClickListener(v -> exportToPDF());

        priceListService = new PriceListService();

        loadPriceList();

        return view;
    }

    private void loadPriceList() {
        String providerId = JwtTokenUtil.getUserId();
        priceListService.getPriceList(providerId, new Callback<List<PriceListItem>>() {
            @Override
            public void onResponse(Call<List<PriceListItem>> call, Response<List<PriceListItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    priceList = response.body();
                    adapter = new PriceListAdapter(priceList, priceListService);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<PriceListItem>> call, Throwable t) {
                Log.e("PriceListFragment", "Error loading price list", t);
            }
        });
    }

    private void exportToPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        paint.setTextSize(16);
        canvas.drawText("Price List", 20, 40, paint);

        int y = 80;

        for (PriceListItem item : priceList) {
            canvas.drawText(item.getName() + " - €" + item.getPrice() + " - Discount: " + item.getDiscount() + "%", 20, y, paint);
            y += 30;
        }

        pdfDocument.finishPage(page);

        File file = new File(requireContext().getExternalFilesDir(null), "PriceList.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getContext(), "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("PriceListFragment", "Error writing PDF", e);
        } finally {
            pdfDocument.close();
        }
    }
}
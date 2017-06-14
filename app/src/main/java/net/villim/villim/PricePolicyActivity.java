package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_ADDITIONAL_GUEST_FEE;
import static net.villim.villim.VillimKeys.KEY_CLEANING_FEE;
import static net.villim.villim.VillimKeys.KEY_DEPOSIT;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_NIGHT;

public class PricePolicyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button closeButton;

    private TextView basePrice;
    private TextView deposit;
    private TextView additionalGuestFee;
    private TextView cleaningFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_policy);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Back button */
        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 기본 가격 */
        basePrice = (TextView) findViewById(R.id.base_price_content);
        String basePriceText = String.format(getString(R.string.won_text_format),
                getIntent().getIntExtra(KEY_RATE_PER_NIGHT, 0));
        basePrice.setText(basePriceText);

        /* 보증금 */
        deposit = (TextView) findViewById(R.id.deposit_content);
        int depositVal = getIntent().getIntExtra(KEY_DEPOSIT, 0);
        String depositText;
        switch (depositVal) {
            case -1:
                depositText = getString(R.string.cant_query);
                break;
            case 0:
                depositText = getString(R.string.no_deposit);
                break;
            default:
                depositText = String.format(getString(R.string.won_symbol_format), depositVal);
                break;
        }
        deposit.setText(depositText);

        /* 추가 인원 요금 */
        additionalGuestFee = (TextView) findViewById(R.id.additional_guest_fee_content);
        int additionalGuestFeeVal = getIntent().getIntExtra(KEY_ADDITIONAL_GUEST_FEE, 0);
        String additionalGuestFeeText;
        switch (additionalGuestFeeVal) {
            case -1:
                additionalGuestFeeText = getString(R.string.cant_query);
                break;
            case 0:
                additionalGuestFeeText = getString(R.string.no_additional_fee);
                break;
            default:
                additionalGuestFeeText = String.format(getString(R.string.won_symbol_format), additionalGuestFeeVal);
                break;
        }
        additionalGuestFee.setText(additionalGuestFeeText);

        /* 청소비 */
        cleaningFee = (TextView) findViewById(R.id.cleaning_fee_content);
        int cleaningFeeVal = getIntent().getIntExtra(KEY_CLEANING_FEE, 0);
        String cleaningFeeText;
        switch (cleaningFeeVal) {
            case -1:
                cleaningFeeText = getString(R.string.cant_query);
                break;
            case 0:
                cleaningFeeText = getString(R.string.no_additional_fee);
                break;
            default:
                cleaningFeeText = String.format(getString(R.string.won_symbol_format), cleaningFeeVal);
                break;
        }
        cleaningFee.setText(cleaningFeeText);
    }
}

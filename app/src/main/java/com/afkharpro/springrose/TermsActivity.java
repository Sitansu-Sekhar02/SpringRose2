package com.afkharpro.springrose;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;

public class TermsActivity extends AppCompatActivity {
    App app = App.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        Typeface face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getApplicationContext().getString(R.string.Terms));
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float) 24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);


        TextView terms = (TextView) findViewById(R.id.t2);


//        if (app.getLanguage().equals("en-GB")){
//            terms.setText(Html.fromHtml("Dear guest, thanks for your visit to Spring Rose store, these condition and terms protect you and us, kindly read it carefully before make an order. Once you make an order that’s mean you agree condition and terms:<br>" +
//                    "<br>" +
//                    "<br>" +
//
//                    "Spring Rose has all rights to change these conditions and terms any time without notice<br>" +
//                    " <br>" +
//                    "<b>Products Availability:</b><br>" +
//                    "<br>" +
//                    "We always care about availability for all products, but for some reasons out of our control, we can’t grantee all flowers and colors all times, and we will contact you and suggest the best option for you once you make an order.<br>" +
//                    " <br>" +
//                    "<b>Delivery:</b><br>" +
//                    "<br>" +
//                    "We take responsibility to deliver all orders on time during work hours (Sat to Thu: 9 a.m. -<br>" +
//                    "11 p.m., Fri: 4-11 p.m.), and we will be ready to deliver your order within hours during work times, and sometimes are facing some challenges in rush times that may effect on your delivery time, and we will contact you in case there are any change or difficulties"));
//
//        }else{
//            terms.setText(Html.fromHtml("عزيزي الضيف الكريم: نشكر لك زيارتك لمتجر زهرة الربيع. هذه الشروط والأحكام توفر الحماية لنا ولعملائنا الكرام ولأي زائر يستخدم هذا الموقع. الرجاء قراءة الشروط التالية بعناية قبل استخدام الموقع. يوافق المستخدم على أن الوصول إلى الموقع واستخدامه يخضع للشروط والأحكام أدناه. إذا كان لديك اعتراض على أحد هذه البنود فنرجو منك التواصل معنا قبل إتمام عملية الشراء<br>" +
//                    "<br>" +
//                    "ويحتفظ متجر زهرة الربيع بالحق في تغيير أجزاء من شروط الاستخدام والأحكام أو تعديلها أو إضافة البعض منها أو إزالتها في أي وقت من الأوقات. وتصبح التغييرات سارية المفعول عندما يتم نشرها على الموقع من دون سابق إنذار. لذلك، يرجى مراجعة شروط الاستخدام والأحكام بشكل منتظم لمعرفة كل التحديثات. يشكل استخدامك المستمر للموقع موافقتك على كل التغييرات التي يجري إحداثها على الشروط والأحكام.<br>" +
//                    "<br>" +
//                    "<br>" +
//
//                    "<br><b>" + "توفير المنتجات " + "</b><br>"+
//                    "نحرص في زهرة الربيع على توفير جميع الطلبات للعملاء ، لكن قد لا يتطابق نوع أو لون المنتج المطلوب مع الصورة 100% وذلك لأسباب منها عدم توفره عند الطلب وصعوبة الحصول على بعض الأنواع والألوان في بعض المواسم من السنة، وفي حال حدوث هذا سنحرص على إبلاعكم بهذا في هذه الحالة والتواصل معكم بعد إتمامكم للطلب. <br>" +
//                    "<br>" +
//                    "<br>" +
//                    "<br><b>" +"التوصيل " +"</b><br>"+
//                    "نتكفل نحن في زهرة الربيع بتوصيل جميع الطلبات للعملاء خلال ساعات العمل الرسمية (من السبت إلى الخميس من التاسعة صباحًا حتى الحادية عشرة مساءً ، يوم الجمعة من الرابعة عصرًا حتى الحادية عشرة مساءً) ونحرص على توصيل جميع الطلبات خلال ساعات ضمن أوقات العمل فور طلبها. مع ذلك قد نعاني من ضغط طلبات في بعض أوقات الذروة مما سيؤدي إلى تأخير بعض الطلبات.<br>" +
//                    "<br>" +
//                    "<br><b>" + "إخلاء المسؤولية"  +"</b><br>"+
//                    "<br>" +
//                    "يرسل متجر زهرة الربيع المنتجات خلال ساعات العمل الرسمية ، ويتكفل المتجر بشكل كامل بالعناية بالطلب وإيصاله بشكل مجاني وبدون أي رسوم إضافية خلال ساعات العمل إلى عنوان العميل، ونحرص أشد الحرص على استلامه لها. إلا أن متجر زهرة الربيع يخلي مسؤوليته في حال خطأ المستخدم في إدخال المعلومات الشخصية ومعلومات التواصل والعنوان، أو في حال عدم استلام العميل للطلب من قِبل فريق العمل."));
//
//        }


        if (app.getLanguage().equals("en-GB")) {
//            terms.setText("Please read carefully. By using our website, your are clearly acknowledging that you have read and understood these terms and by selecting “I agree to the terms & conditions and Privacy Policy” you accept and agree to bound by these online shopping terms and conditions.  We reserve the right to change these terms and conditions at any time. We will post any changes on the Site and it is your responsibility as a customer to review the Terms and Conditions on each occasion you access the Online Service or Online Site. Your continuous usage to our website after we've made changes will mean that you agree to the changes. Please also review the Privacy Policy, which set our data collection and usage practices. \n" +
//                    "\n" +
//                    "Our Products\n" +
//                    "Our main products are flower arrangements that are subject to availability. We are regularly updating our products. Should any of the products mentioned be unavailable, we will contact you using the contact details you provided upon placing your order and either offer you an alternative item of equivalent quality and price if this is possible; or Cancel the item from your order and give you a full refund in respect of that item. or If we are unable to contact you or do not receive a response from you, we reserve the right to substitute a product of equivalent value, quality and color and will process any remaining items on your order. As the main category of the arrangement are fresh flowers, all orders placed online cannot be exchanged or returned.\n" +
//                    "\n" +
//                    "Your Account\n" +
//                    "To fulfill your order, you need to create an account with accurate and complete information to purchase items using this website. Inform us immediately of any changes to those details (e.g. change of email or phone number). You are responsible for safeguarding your account, user name, and password and for preventing unauthorized access to your profile. You agree to accept responsibility for all activities that occur under your account or password. Please inform us immediately if you have any reason to believe that your user name and/or password has become known to anyone else, or are being, or are likely to be, used in an unauthorized manner.\n" +
//                    "Please ensure the details you provide us with on registration are correct and complete and). We reserve the right to refuse access to the website, terminate accounts, remove or edit content, or cancel orders at our discretion. If we cancel an order, it will be without charge to you. \n" +
//                    "\n" +
//                    "\n" +
//                    "Prices :\n" +
//                    "All prices are subject to change without prior notice or obligations.\n" +
//                    "Full payment is required when placing your online order and cardholder must keep a copy of transaction records and Merchant policies and rules.\n" +
//                    "To fulfill your order we require all necessary fields contained within the order form to be completed with the necessary information. By submitting your order and payment details, you are consenting to be bound by our terms and conditions contained in these Terms and Conditions and appearing anywhere on the site.\n" +
//                    "any dispute or claim arising out of or in connection with this website shall be governed and construed in accordance with the laws of KSA\".\n" +
//                    "Payment Refund: \n" +
//                    "Refund will be available only in case of unavailability of item ordered. Please note that Refunds will be done only through the original mode of payment\".\n" +
//                    "Claim for refund should be made within 12 hours of actual delivery. No claim will be entertained after 12 hours.\n" +
//                    "\n" +
//                    "On paying via bank transfer, it is your responsibility to make sure that a copy of the transfer showing Spring Rose account number and a successful transaction status before claiming your purchases. \n" +
//                    "Delivery \n" +
//                    "Deliveries are made free of charge 7 days a week from Saturday to Wednesday from 9:30 to 23:00. Friday deliveries start from 16:00 to 23:00.\n" +
//                    "Sender needs to provide the correct address and telephone number of the recipient. the company will not be responsible for any type of compensations, If the provided recipient address is not complete, incorrect and does not answer the phone.\n" +
//                    "\n" +
//                    "Delivery conditions during high seasons may take up to 24 hours, therefore, customers are requested to place their orders 24 hours before the desired delivery time.\n" +
//                    "Cancellations\n" +
//                    "Please note that all our work is made-to-order for that we cannot offer a refund on your purchases if you change your mind.\n" +
//                    "However, if you would like to postpone the delivery of your future order, please give us notice 2 full working days in advance of the original delivery date.\n" +
//                    "If there is a quality issue or if you received a wrong item, please contact us immediately. Describe the problem and where possible, send us an image, so that we can offer you a resolution as efficiently as possible.\n" +
//                    "Incorrect Items\n" +
//                    "If an incorrect item is received by you please notify our Customer Services as soon as possible and they will arrange the pickup. If you would like us to replace the incorrect item with the item you ordered we will send you the correct item as a replacement as soon as possible.\n" +
//                    "Promotions:\n" +
//                    "We publish all promotional materials on our website or through the social media platforms. The customer has the right to benefit from the offer/promotion during the period of this offer only. The customer is not entitled to benefit from more than one promotional offer at once and is not entitled to claim any promotional offer that has previously announced and terminated or removed from the site.\n" +
//                    "Privacy Policy\n" +
//                    "All personal information (name, delivery address, email, contact number, etc.) collected from you is confidential. It is used solely for the purpose of delivery and billing of your order. Any information collected will not be used for internal marketing purposes, and will not be disclosed to any other parties without prior consent of our customers.\n" +
//                    "\n");

            terms.setText(Html.fromHtml("<p>Please read carefully. By using our website, your are clearly acknowledging that you have read and understood these terms and by selecting &ldquo;I agree to the terms &amp; conditions and Privacy Policy&rdquo; you accept and agree to bound by these online shopping terms and conditions.&nbsp; We reserve the right to change these terms and conditions at any time. We will post any changes on the Site and it is your responsibility as a customer to review the Terms and Conditions on each occasion you access the Online Service or Online Site. Your continuous usage to our website after we've made changes will mean that you agree to the changes. Please also review the Privacy Policy, which set our data collection and usage practices.</p>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<h1>Our Products</h1>\n" +
                    "<p>Our main products are flower arrangements that are subject to availability. We are regularly updating our products. Should any of the products mentioned be unavailable, we will contact you using the contact details you provided upon placing your order and either offer you an alternative item of equivalent quality and price if this is possible; or Cancel the item from your order and give you a full refund in respect of that item. or If we are unable to contact you or do not receive a response from you, we reserve the right to substitute a product of equivalent value, quality and color and will process any remaining items on your order. As the main category of the arrangement are fresh flowers, all orders placed online cannot be exchanged or returned.</p>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<h1>Your Account</h1>\n" +
                    "<p>To fulfill your order, you need to create an account with accurate and complete information to purchase items using this website. Inform us immediately of any changes to those details (e.g. change of email or phone number). You are responsible for safeguarding your account, user name, and password and for preventing unauthorized access to your profile. You agree to accept responsibility for all activities that occur under your account or password. Please inform us immediately if you have any reason to believe that your user name and/or password has become known to anyone else, or are being, or are likely to be, used in an unauthorized manner.</p>\n" +
                    "<p>Please ensure the details you provide us with on registration are correct and complete and). We reserve the right to refuse access to the website, terminate accounts, remove or edit content, or cancel orders at our discretion. If we cancel an order, it will be without charge to you.</p>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<h1>&nbsp;</h1>\n" +
                    "<h1>Prices :</h1>\n" +
                    "<p>All prices are subject to change without prior notice or obligations.</p>\n" +
                    "<p>Full payment is required when placing your online order and cardholder must keep a copy of transaction records and Merchant policies and rules.</p>\n" +
                    "<p>To fulfill your order we require all necessary fields contained within the order form to be completed with the necessary information. By submitting your order and payment details, you are consenting to be bound by our terms and conditions contained in these Terms and Conditions and appearing anywhere on the site.</p>\n" +
                    "<p>any dispute or claim arising out of or in connection with this website shall be governed and construed in accordance with the laws of KSA\".</p>\n" +
                    "<h1>Payment Refund:</h1>\n" +
                    "<p>Refund will be available only in case of unavailability of item ordered. Please note that Refunds will be done only through the original mode of payment\".</p>\n" +
                    "<p>Claim for refund should be made within 12 hours of actual delivery. No claim will be entertained after 12 hours.</p>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<p>On paying via bank transfer, it is your responsibility to make sure that a copy of the transfer showing Spring Rose account number and a successful transaction status before claiming your purchases.</p>\n" +
                    "<h1>Delivery</h1>\n" +
                    "<p>Deliveries are made free of charge 7 days a week from Saturday to Wednesday from 9:30 to 23:00. Friday deliveries start from 16:00 to 23:00.</p>\n" +
                    "<p>Sender needs to provide the correct address and telephone number of the recipient. the company will not be responsible for any type of compensations, If the provided recipient address is not complete, incorrect and does not answer the phone.</p>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<p>Delivery conditions during high seasons may take up to 24 hours, therefore, customers are requested to place their orders 24 hours before the desired delivery time.</p>\n" +
                    "<h1>Cancellations</h1>\n" +
                    "<p>Please note that all our work is made-to-order for that we cannot offer a refund on your purchases if you change your mind.</p>\n" +
                    "<p>However, if you would like to postpone the delivery of your future order, please give us notice 2 full working days in advance of the original delivery date.</p>\n" +
                    "<p>If there is a quality issue or if you received a wrong item, please contact us immediately. Describe the problem and where possible, send us an image, so that we can offer you a resolution as efficiently as possible.</p>\n" +
                    "<h1>Incorrect Items</h1>\n" +
                    "<p>If an incorrect item is received by you please notify our Customer Services as soon as possible and they will arrange the pickup. If you would like us to replace the incorrect item with the item you ordered we will send you the correct item as a replacement as soon as possible.</p>\n" +
                    "<h1>Promotions:</h1>\n" +
                    "<p>We publish all promotional materials on our website or through the social media platforms. The customer has the right to benefit from the offer/promotion during the period of this offer only. The customer is not entitled to benefit from more than one promotional offer at once and is not entitled to claim any promotional offer that has previously announced and terminated or removed from the site.</p>\n" +
                    "<h1>Privacy Policy</h1>\n" +
                    "<p>All personal information (name, delivery address, email, contact number, etc.) collected from you is confidential. It is used solely for the purpose of delivery and billing of your order. Any information collected&nbsp;will not be used for internal marketing purposes, and will not be disclosed to any other parties without prior consent of our customers.</p>\n" +
                    "<p>&nbsp;</p>"));
        } else {
//            terms.setText("يرجى القراءة بعناية. من خلال استخدام موقعنا الإلكتروني، فإنك تقر بوضوح بأنك قد قرأت وفهمت هذه الشروط وعن طريق اختيار \"أوافق على الشروط والأحكام وسياسة الخصوصية\" فإنك تقبل وتوافق على الالتزام بهذه الشروط والأحكام للتسوق عبر الإنترنت. الشركة تحتفظ بالحق في تغيير هذه الشروط والأحكام في أي وقت. سنقوم بنشر أي تغييرات على الموقع، ومن مسؤوليتكم كعميل مراجعة الشروط والأحكام في كل مرة يمكنك الوصول إلى الخدمة عبر الإنترنت أو موقع على شبكة الإنترنت. إن استخدامك المستمر لموقعنا على الويب بعد إجراء التغييرات يعني أنك توافق على التغييرات. يرجى أيضا مراجعة سياسة الخصوصية التي تحدد ممارسات جمع البيانات واستخدامها.\n" +
//                    "\n" +
//                    "منتجاتنا\n" +
//                    "منتجاتنا الرئيسية هي تنسيق الزهور التي هي رهن بتوفر المنتجات. نحن بانتظام نقوم بتحديث منتجاتنا. إذا كان أي من المنتجات المذكورة غير متوفرة، سوف نتصل بك باستخدام تفاصيل الاتصال التي قدمتها عند تقديم طلبك وإما:\n" +
//                    "نقدم لك بندا بديلا من الجودة والسعر المكافئ إذا كان ذلك ممكنا؛ أو إلغاء طلبك مع حقك في استزداد كامل المبلغ فيما يتعلق بهذا البند. أو إذا كنا غير قادرين على الاتصال بك أو لا تتلقى ردا منك، فنحن نحتفظ بالحق في استبدال منتج ما يعادل القيمة والجودة واللون، وسوف يتم معالجة أي من العناصر المتبقية  لطلبك مع حقك في استرداد قيمة العناصر التي كنا غير قادرين تزويدها. لا يمكن استبدال الطلبات المقدمة عبر الإنترنت.\n" +
//                    "\n" +
//                    "الحساب الخاص بك\n" +
//                    "لتلبية طلبك، تحتاج إلى إنشاء حساب مع معلومات دقيقة وكاملة لشراء العناصر باستخدام هذا الموقع. يرجى إبلاغنا فورا بأي تغييرات في تلك التفاصيل (مثل تغيير البريد الإلكتروني أو الهاتف أو غيرها). أنت مسؤول عن حماية حسابك واسم المستخدم وكلمة المرور ولمنع الوصول غير المصرح به إلى ملفك الشخصي. أنت توافق على قبول المسؤولية عن جميع الأنشطة التي تحدث في حسابك أو كلمة المرور. يرجى إبلاغنا على الفور إذا كان لديك أي سبب للاعتقاد بأن اسم المستخدم و / أو كلمة المرور أصبح معروفا لأي شخص آخر أو من المرجح أن يكون، بطريقة غير مصرح بها.\n" +
//                    "يرجى التأكد من التفاصيل التي تقدمها لنا مع تسجيل بيانات صحيحة وكاملة. نحن نحتفظ بالحق في رفض وصولك إلى الموقع، وإنهاء الحسابات، وإزالة أو تعديل المحتوى، أو إلغاء أوامر وفقا لتقديرنا وذلك سيكون دون مطالبتك بأي تكلفة.\n" +
//                    "\n" +
//                    "الأسعار\n" +
//                    "جميع الأسعار عرضة للتغيير دون إشعار مسبق أو التزامات.\n" +
//                    "سوف يكون مطلوب منك دفع كامل القيمة لمشترياتك عند طلبك من الانترنت وحامل البطاقة يجب الاحتفاظ بنسخة من سجلات المعاملات والسياسات والقواعد.\n" +
//                    "لتلبية طلبك نطلب منك أن تملي جميع الحقول اللازمة الواردة في نموذج الطلب إلى أن تكتمل مع المعلومات اللازمة. من خلال تقديم طلبك وتفاصيل الدفع، فإنك توافق على الالتزام بالبنود والشروط الواردة في هذه الشروط والأحكام والتي تظهر في أي مكان على الموقع.\n" +
//                    "أي نزاع أو مطالبة تنشأ عن أو فيما يتعلق بهذا الموقع يجب أن تخضع وتفسر وفقا لقوانين المملكة العربية السعودية “.\n" +
//                    "\n" +
//                    "استرداد المدفوعات\n" +
//                    "سوف يكون متاح استرداد قيمة المشتريات فقط في حالة عدم توافر كامل طلبك. يرجى ملاحظة أن استرداد المبالغ سيتم فقط من خلال طريقة الدفع الأصلية .\n" +
//                    "المطالبة باسترداد قيمة المشتريات ينبغي أن يتم في غضون 12 ساعة من تقديم طلبك ولن يكون متاحا مطلقا أي مطالبة بعد 12 ساعة.\n" +
//                    "\n" +
//                    "في حال الدفع عن طريق تحويل بنكي، الرجاء ارسال صورة عن التحويل موضح فيها رقم الحساب لشركة زهرة الربيع وحالة العملية تمت بنجاح.\n" +
//                    "\n" +
//                    "خدمة التوصيل\n" +
//                    "يتم التسليم مجانا 7 أيام في الأسبوع من السبت إلى الأربعاء من الساعة 9:30 صباحا وحتى 11:00 ليلا. تبدأ عمليات التسليم ليوم الجمعة من الساعة 16:00 حتى 23:00.\n" +
//                    "المرسل يجب عليه تقديم العنوان الصحيح ورقم هاتف المستلم والشركة لن تكون مسؤولة عن أي نوع من التعويضات، إذا كان عنوان المستلم  أو بياناته المقدمة ليست كاملة، غير صحيحة أو لا يجيب على الهاتف.\n" +
//                    "\n" +
//                    "في حال الطلب خلال فترة الأعياد أو المواسم، سوف يتم تجهيز الطلبات خلال فترة ٢٤ ساعة، لذلك ننصح العملاء بتقديم طلباتهم قبل ٢٤ ساعة من تاريخ التوصيل المطلوب.\n" +
//                    "\n" +
//                    "إلغاء الطلبات\n" +
//                    "يرجى ملاحظة أن جميع أعمالنا يتم تجهيزها حسب الطلب بحيث لا يمكننا تقديم رد قيمة مشترياتك في حال غيرت رأيك.\n" +
//                    "ومع ذلك، إذا كنت ترغب في تأجيل تسليم طلبك، يرجى إعطائنا إشعار قبل يومين عمل  مقدما من تاريخ التسليم الأصلي.\n" +
//                    "إذا كان هناك مشكلة في الجودة أو إذا كنت قد تلقيت الطلب الخاطئ، يرجى الاتصال بنا على الفور. ووصف المشكلة. وحيثما أمكن يرجى إرسال لنا صورة، حتى نتمكن من تقديم لكم الخدمة بأكبر قدر ممكن من الكفاءة.\n" +
//                    "\n" +
//                    "طلب غير صحيح\n" +
//                    "إذا تم استلام طلب غير صحيح من قبلك يرجى إبلاغ خدمة العملاء لدينا في أقرب وقت ممكن، وسوف يتم اتخاذ التدابير اللازمة من قبلنا. إذا كنت تريد منا استبدال الطلبات الغير صحيحة، سوف نرسل لك الطلب الصحيح في أقرب وقت ممكن.\n" +
//                    "\n" +
//                    "العروض\n" +
//                    "يعلن عن أي عروض السارية على الموقع الإلكتروني أو من خلال منصات التواصل الاجتماعي، ويحق للعميل الإستفادة من العرض في فترة عرضه فقط، و لا يحق للعميل الاستفادة من أكثر من عرض ترويجي في ان واحد ولا يحق له المطالبة بأي عرض ترويجي قد سبق الإعلان عنه وتم انتهاءه أو رفعه من الموقع.\n" +
//                    "\n" +
//                    "سياسة الخصوصية\n" +
//                    "تعتبر جميع المعلومات الشخصية (الاسم، عنوان التسليم، والبريد الإلكتروني، ورقم الاتصال، الخ) التي تم جمعها منك سرية. يتم استخدامها فقط لغرض التسليم ووتقديم الخدمة وفقا لطلبك. لن يتم استخدام أي معلومات تم جمعها لأغراض التسويق الداخلي، ولن يتم الكشف عنها لأي أطراف أخرى دون موافقة مسبقة من عملائنا.\n" +
//                    "\n");

            terms.setText(Html.fromHtml("<p style=\"text-align: right;\">يرجى القراءة بعناية. من خلال استخدام موقعنا الإلكتروني، فإنك تقر بوضوح بأنك قد قرأت وفهمت هذه الشروط وعن طريق اختيار \"أوافق على الشروط والأحكام وسياسة الخصوصية\" فإنك تقبل وتوافق على الالتزام بهذه الشروط والأحكام للتسوق عبر الإنترنت. الشركة تحتفظ بالحق في تغيير هذه الشروط والأحكام في أي وقت. سنقوم بنشر أي تغييرات على الموقع، ومن مسؤوليتكم كعميل مراجعة الشروط والأحكام في كل مرة يمكنك الوصول إلى الخدمة عبر الإنترنت أو موقع على شبكة الإنترنت. إن استخدامك المستمر لموقعنا على الويب بعد إجراء التغييرات يعني أنك توافق على التغييرات. يرجى أيضا مراجعة سياسة الخصوصية التي تحدد ممارسات جمع البيانات واستخدامها.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>منتجاتنا</strong></p>\n" +
                    "<p style=\"text-align: right;\">منتجاتنا الرئيسية هي تنسيق الزهور التي هي رهن بتوفر المنتجات. نحن بانتظام نقوم بتحديث منتجاتنا. إذا كان أي من المنتجات المذكورة غير متوفرة، سوف نتصل بك باستخدام تفاصيل الاتصال التي قدمتها عند تقديم طلبك وإما:</p>\n" +
                    "<p style=\"text-align: right;\">نقدم لك بندا بديلا من الجودة والسعر المكافئ إذا كان ذلك ممكنا؛ أو إلغاء طلبك مع حقك في استزداد كامل المبلغ فيما يتعلق بهذا البند. أو إذا كنا غير قادرين على الاتصال بك أو لا تتلقى ردا منك، فنحن نحتفظ بالحق في استبدال منتج ما يعادل القيمة والجودة واللون، وسوف يتم معالجة أي من العناصر المتبقية &nbsp;لطلبك مع حقك في استرداد قيمة العناصر التي كنا غير قادرين تزويدها. لا يمكن استبدال الطلبات المقدمة عبر الإنترنت.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>الحساب الخاص بك</strong></p>\n" +
                    "<p style=\"text-align: right;\">لتلبية طلبك، تحتاج إلى إنشاء حساب مع معلومات دقيقة وكاملة لشراء العناصر باستخدام هذا الموقع. يرجى إبلاغنا فورا بأي تغييرات في تلك التفاصيل (مثل تغيير البريد الإلكتروني أو الهاتف أو غيرها). أنت مسؤول عن حماية حسابك واسم المستخدم وكلمة المرور ولمنع الوصول غير المصرح به إلى ملفك الشخصي. أنت توافق على قبول المسؤولية عن جميع الأنشطة التي تحدث في حسابك أو كلمة المرور. يرجى إبلاغنا على الفور إذا كان لديك أي سبب للاعتقاد بأن اسم المستخدم و / أو كلمة المرور أصبح معروفا لأي شخص آخر أو من المرجح أن يكون، بطريقة غير مصرح بها.</p>\n" +
                    "<p style=\"text-align: right;\">يرجى التأكد من التفاصيل التي تقدمها لنا مع تسجيل بيانات صحيحة وكاملة. نحن نحتفظ بالحق في رفض وصولك إلى الموقع، وإنهاء الحسابات، وإزالة أو تعديل المحتوى، أو إلغاء أوامر وفقا لتقديرنا وذلك سيكون دون مطالبتك بأي تكلفة.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>الأسعار</strong></p>\n" +
                    "<p style=\"text-align: right;\">جميع الأسعار عرضة للتغيير دون إشعار مسبق أو التزامات.</p>\n" +
                    "<p style=\"text-align: right;\">سوف يكون مطلوب منك دفع كامل القيمة لمشترياتك عند طلبك من الانترنت وحامل البطاقة يجب الاحتفاظ بنسخة من سجلات المعاملات والسياسات والقواعد.</p>\n" +
                    "<p style=\"text-align: right;\">لتلبية طلبك نطلب منك أن تملي جميع الحقول اللازمة الواردة في نموذج الطلب إلى أن تكتمل مع المعلومات اللازمة. من خلال تقديم طلبك وتفاصيل الدفع، فإنك توافق على الالتزام بالبنود والشروط الواردة في هذه الشروط والأحكام والتي تظهر في أي مكان على الموقع.</p>\n" +
                    "<p style=\"text-align: right;\">أي نزاع أو مطالبة تنشأ عن أو فيما يتعلق بهذا الموقع يجب أن تخضع وتفسر وفقا لقوانين المملكة العربية السعودية &ldquo;.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>استرداد المدفوعات</strong></p>\n" +
                    "<p style=\"text-align: right;\">سوف يكون متاح استرداد قيمة المشتريات فقط في حالة عدم توافر كامل طلبك. يرجى ملاحظة أن استرداد المبالغ سيتم فقط من خلال طريقة الدفع الأصلية .</p>\n" +
                    "<p style=\"text-align: right;\">المطالبة باسترداد قيمة المشتريات ينبغي أن يتم في غضون 12 ساعة من تقديم طلبك ولن يكون متاحا مطلقا أي مطالبة بعد 12 ساعة.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\">في حال الدفع عن طريق تحويل بنكي، الرجاء ارسال صورة عن التحويل موضح فيها رقم الحساب لشركة زهرة الربيع وحالة العملية تمت بنجاح.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>خدمة التوصيل</strong></p>\n" +
                    "<p style=\"text-align: right;\">يتم التسليم مجانا 7 أيام في الأسبوع من السبت إلى الأربعاء من الساعة 9:30 صباحا وحتى 11:00 ليلا. تبدأ عمليات التسليم ليوم الجمعة من الساعة 16:00 حتى 23:00.</p>\n" +
                    "<p style=\"text-align: right;\">المرسل يجب عليه تقديم العنوان الصحيح ورقم هاتف المستلم والشركة لن تكون مسؤولة عن أي نوع من التعويضات، إذا كان عنوان المستلم &nbsp;أو بياناته المقدمة ليست كاملة، غير صحيحة أو لا يجيب على الهاتف.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\">في حال الطلب خلال فترة الأعياد أو المواسم، سوف يتم تجهيز الطلبات خلال فترة ٢٤ ساعة، لذلك ننصح العملاء بتقديم طلباتهم قبل ٢٤ ساعة من تاريخ التوصيل المطلوب.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>إلغاء الطلبات</strong></p>\n" +
                    "<p style=\"text-align: right;\">يرجى ملاحظة أن جميع أعمالنا يتم تجهيزها حسب الطلب بحيث لا يمكننا تقديم رد قيمة مشترياتك في حال غيرت رأيك.</p>\n" +
                    "<p style=\"text-align: right;\">ومع ذلك، إذا كنت ترغب في تأجيل تسليم طلبك، يرجى إعطائنا إشعار قبل يومين عمل &nbsp;مقدما من تاريخ التسليم الأصلي.</p>\n" +
                    "<p style=\"text-align: right;\">إذا كان هناك مشكلة في الجودة أو إذا كنت قد تلقيت الطلب الخاطئ، يرجى الاتصال بنا على الفور. ووصف المشكلة. وحيثما أمكن يرجى إرسال لنا صورة، حتى نتمكن من تقديم لكم الخدمة بأكبر قدر ممكن من الكفاءة.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>طلب غير صحيح</strong></p>\n" +
                    "<p style=\"text-align: right;\">إذا تم استلام طلب غير صحيح من قبلك يرجى إبلاغ خدمة العملاء لدينا في أقرب وقت ممكن، وسوف يتم اتخاذ التدابير اللازمة من قبلنا. إذا كنت تريد منا استبدال الطلبات الغير صحيحة، سوف نرسل لك الطلب الصحيح في أقرب وقت ممكن.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>العروض</strong></p>\n" +
                    "<p style=\"text-align: right;\">يعلن عن أي عروض السارية على الموقع الإلكتروني أو من خلال منصات التواصل الاجتماعي، ويحق للعميل الإستفادة من العرض في فترة عرضه فقط، و لا يحق للعميل الاستفادة من أكثر من عرض ترويجي في ان واحد ولا يحق له المطالبة بأي عرض ترويجي قد سبق الإعلان عنه وتم انتهاءه أو رفعه من الموقع.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: right;\"><strong>سياسة الخصوصية</strong></p>\n" +
                    "<p style=\"text-align: right;\">تعتبر جميع المعلومات الشخصية (الاسم، عنوان التسليم، والبريد الإلكتروني، ورقم الاتصال، الخ) التي تم جمعها منك سرية. يتم استخدامها فقط لغرض التسليم ووتقديم الخدمة وفقا لطلبك. لن يتم استخدام أي معلومات تم جمعها لأغراض التسويق الداخلي، ولن يتم الكشف عنها لأي أطراف أخرى دون موافقة مسبقة من عملائنا.</p>\n" +
                    "<p style=\"text-align: right;\">&nbsp;</p>"));
        }

    }
}
package com.udl.bss.barbershopschedule.database;

import android.content.Context;

import com.udl.bss.barbershopschedule.domain.Appointment;
import com.udl.bss.barbershopschedule.domain.Barber;
import com.udl.bss.barbershopschedule.domain.BarberService;
import com.udl.bss.barbershopschedule.domain.Client;
import com.udl.bss.barbershopschedule.domain.Promotion;
import com.udl.bss.barbershopschedule.domain.Review;
import com.udl.bss.barbershopschedule.domain.Schedule;
import com.udl.bss.barbershopschedule.domain.SpecialDay;
import com.udl.bss.barbershopschedule.webService.SIL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alex on 24/11/2017.
 */

public class BLL {
    private DAL dal_instance = null;

    public BLL(Context context) {
        this.dal_instance = DAL.GetInstance(context);
    }

    public ArrayList<Barber> Get_BarberShopList(boolean need_refresh) {
        //TODO
        if (need_refresh) {
            Delete_AllBarberShops();
            Delete_Appointments();
            Delete_Promotions();
            Delete_Reviews();
            Delete_Schedules();
            Delete_Services();
            Delete_SpecialDays();

            Initialize_Database();
        }

        return this.dal_instance.Get_BarberShopList();
    }

    public Barber Get_BarberShop(int barber_shop_id) {
        return this.dal_instance.Get_BarberShop(barber_shop_id);
    }

    public void Insert_BarberShop(Barber new_barber_shop) {
        this.dal_instance.Insert_BarberShop(new_barber_shop);
    }

    public void Insert_BarberShops(ArrayList<Barber> new_barber_shops) {
        this.dal_instance.Insert_BarberShops(new_barber_shops);
    }

    public void Delete_AllBarberShops() {
        this.dal_instance.Delete_AllBarberShops();
    }

    public ArrayList<Client> Get_ClientList() {
        return this.dal_instance.Get_ClientList();
    }

    public Client Get_Client(int client_id) {
        return this.dal_instance.Get_Client(client_id);
    }

    public void Insert_Client(Client new_client) {
        this.dal_instance.Insert_Client(new_client);
    }

    public void Delete_AllClients() {
        this.dal_instance.Delete_AllClients();
    }

    public ArrayList<Review> Get_BarberShopReviews(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopReviews(barber_shop_id);
    }

    public void Insert_Reviews(ArrayList<Review> reviews) {
        this.dal_instance.Insert_Reviews(reviews);
    }

    public void Delete_Reviews() {
        this.dal_instance.Delete_Reviews();
    }

    public ArrayList<BarberService> Get_BarberShopServices(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopServices(barber_shop_id);
    }

    public BarberService Get_BarberShopService(int service_id) {
        return this.dal_instance.Get_BarberShopService(service_id);
    }

    public void Insert_Services(ArrayList<BarberService> services) {
        this.dal_instance.Insert_Services(services);
    }

    public void Delete_Services() {
        this.dal_instance.Delete_Services();
    }

    public ArrayList<Appointment> Get_BarberShopAppointments(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopAppointments(barber_shop_id);
    }

    public ArrayList<Appointment> Get_ClientAppointments(int client_id) {
        return this.dal_instance.Get_ClientAppointments(client_id);
    }

    public void Insert_Appointments(ArrayList<Appointment> appointments) {
        this.dal_instance.Insert_Appointments(appointments);
    }

    public void Delete_Appointments() {
        this.dal_instance.Delete_Appointments();
    }

    public ArrayList<Promotion> Get_BarberShopPromotions(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopPromotions(barber_shop_id);
    }

    public void Insert_Promotions(ArrayList<Promotion> promotions) {
        this.dal_instance.Insert_Promotions(promotions);
    }

    public void Delete_Promotions() {
        this.dal_instance.Delete_Promotions();
    }

    public ArrayList<SpecialDay> Get_BarberShopSpecialDays(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopSpecialDays(barber_shop_id);
    }

    public void Insert_SpecialDays(ArrayList<SpecialDay> special_days) {
        this.dal_instance.Insert_SpecialDays(special_days);
    }

    public void Delete_SpecialDays() {
        this.dal_instance.Delete_SpecialDays();
    }

    public ArrayList<Schedule> Get_BarberShopSchedules(int barber_shop_id) {
        return this.dal_instance.Get_BarberShopSchedules(barber_shop_id);
    }

    public void Insert_Schelues(ArrayList<Schedule> schedules) {
        this.dal_instance.Insert_Schelues(schedules);
    }

    public void Delete_Schedules() {
        this.dal_instance.Delete_Schedules();
    }

    //TODO
    public void Initialize_Database() {

        if (Get_BarberShopList(false).size() == 0) {
            String response = SIL.Get("https://raw.githubusercontent.com/master-eng-inf/BarberShopFakeData/master/Data/barber_shop_list.json");

            if (response != null) {
                ArrayList<Barber> db_barber_shop_list = new ArrayList<>();
                ArrayList<BarberService> db_barber_shop_services = new ArrayList<>();
                ArrayList<Appointment> db_barber_shop_appointments = new ArrayList<>();
                ArrayList<Review> db_barber_shop_reviews = new ArrayList<>();
                ArrayList<Schedule> db_barber_shop_schedules = new ArrayList<>();
                ArrayList<SpecialDay> db_barber_shop_special_days = new ArrayList<>();
                ArrayList<Promotion> db_barber_shop_promotions = new ArrayList<>();

                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray barber_shop_list = root.getJSONArray("barber_shops");

                    // Looping through all barber shops and their related data
                    for (int barber_count = 0; barber_count < barber_shop_list.length(); barber_count++) {
                        JSONObject barber_shop = barber_shop_list.getJSONObject(barber_count);

                        db_barber_shop_list.add(new Barber(barber_shop.getInt("id"),
                                barber_shop.getString("name"), barber_shop.getString("description"),
                                barber_shop.getString("city"), barber_shop.getString("address"),
                                barber_shop.getString("phone"), barber_shop.getString("email"), null));

                        JSONArray schedules = barber_shop.getJSONArray("schedule");
                        for (int schedule_count = 0; schedule_count < schedules.length(); schedule_count++) {
                            JSONObject schedule = schedules.getJSONObject(schedule_count);
                            db_barber_shop_schedules.add(new Schedule(barber_shop.getInt("id"),
                                    schedule.getInt("day_of_week"), schedule.getString("oppening_1"),
                                    schedule.getString("closing_1"), schedule.getString("oppening_2"),
                                    schedule.getString("closing_2"), schedule.getInt("appointments_at_same_time")));
                        }

                        JSONArray special_days = barber_shop.getJSONArray("special_days");
                        for (int special_day_count = 0; special_day_count < special_days.length(); special_day_count++) {
                            JSONObject special_day = special_days.getJSONObject(special_day_count);
                            db_barber_shop_special_days.add(new SpecialDay(barber_shop.getInt("id"),
                                    special_day.getString("date"), special_day.getInt("type_of_day")));
                        }

                        JSONArray services = barber_shop.getJSONArray("services");
                        for (int service_count = 0; service_count < services.length(); service_count++) {
                            JSONObject service = services.getJSONObject(service_count);
                            db_barber_shop_services.add(new BarberService(service.getInt("id"),
                                    barber_shop.getInt("id"), service.getString("name"),
                                    service.getDouble("price"), service.getDouble("duration")));
                        }

                        JSONArray appointments = barber_shop.getJSONArray("appointments");
                        for (int appointment_count = 0; appointment_count < appointments.length(); appointment_count++) {
                            JSONObject appointment = appointments.getJSONObject(appointment_count);
                            db_barber_shop_appointments.add(new Appointment(appointment.getInt("id"), appointment.getInt("client_id"),
                                    barber_shop.getInt("id"), appointment.getInt("service_id"),
                                    appointment.getInt("promotion_id"), appointment.getString("date")));
                        }

                        JSONArray promotions = barber_shop.getJSONArray("promotions");
                        for (int promotion_count = 0; promotion_count < promotions.length(); promotion_count++) {
                            JSONObject promotion = promotions.getJSONObject(promotion_count);
                            db_barber_shop_promotions.add(new Promotion(promotion.getInt("id"), barber_shop.getInt("id"),
                                    promotion.getInt("service_id"), promotion.getString("name"), promotion.getString("description")));
                        }

                        JSONArray reviews = barber_shop.getJSONArray("reviews");
                        for (int review_count = 0; review_count < reviews.length(); review_count++) {
                            JSONObject review = reviews.getJSONObject(review_count);
                            db_barber_shop_reviews.add(new Review(review.getInt("client_id"), barber_shop.getInt("id"),
                                    review.getString("review_description"), review.getDouble("review_mark"),
                                    review.getString("review_date")));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Insert_BarberShops(db_barber_shop_list);
                Insert_Appointments(db_barber_shop_appointments);
                Insert_Reviews(db_barber_shop_reviews);
                Insert_Services(db_barber_shop_services);
                Insert_Promotions(db_barber_shop_promotions);
                Insert_SpecialDays(db_barber_shop_special_days);
                Insert_Schelues(db_barber_shop_schedules);
            }
        }
    }
}
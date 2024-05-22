var currencySymbol = "[[${CURRENCY_SYMBOL}]]";
var thousandsPointType = "[[${THOUSANDS_POINT_TYPE == 'COMMA' ? ',' : '.'}]]";
var decimalPointType = "[[${DECIMAL_POINT_TYPE == 'COMMA' ? ',' : '.'}]]";
var currencySymbolPosition = "[[${CURRENCY_SYMBOL_POSITION}]]";
var decimalDigits = "[[${DECIMAL_DIGITS}]]";

var prefixCurrencySymbol = currencySymbolPosition === 'Before price' ? currencySymbol : '';
var suffixCurrencySymbol = currencySymbolPosition === 'After price' ? currencySymbol : '';
function filterSales(filterType) {
    var filterText = document.getElementById('filterText');
    var salesCount = document.getElementById('salesCount');

    switch (filterType) {
        case 'today':
            filterText.innerText = '| Hôm nay';
            salesCount.innerText = '[[${totalOrderToday}]]';
            break;
        case 'thisMonth':
            filterText.innerText = '| Tháng này';
            salesCount.innerText =  '[[${totalOrderMonth}]]';
            break;
        case 'thisYear':
            filterText.innerText = '| Năm nay';
            salesCount.innerText = '[[${totalOrderYear}]]';
            break;
        default:
            break;
    }
}
function formatCurrency(value) {
    return $.number(value, decimalDigits, decimalPointType, thousandsPointType) + suffixCurrencySymbol;
}

function showRevenue(period) {
    let revenueAmount;
    let revenuePeriod;

    // Sử dụng Thymeleaf để nhúng giá trị động vào JavaScript
    const sumOrderToday = [[${sumOrderToday}]] |0;
    const sumOrderLast7Days = [[${sumOrderLast7Days}]]| 0;
    const sumOrderLastMonth = [[${sumOrderLastMonth}]] |0;

    switch (period) {
        case 'today':
            revenueAmount = sumOrderToday;
            revenuePeriod = '| Hôm nay';
            break;
        case 'week':
            revenueAmount = sumOrderLast7Days;
            revenuePeriod = '| Tuần qua';
            break;
        case 'month':
            revenueAmount = sumOrderLastMonth;
            revenuePeriod = '| 1 tháng qua';
            break;
        default:
            revenueAmount = sumOrderToday;
            revenuePeriod =  '| Hôm nay';
    }

    document.getElementById('revenue-amount').innerText = prefixCurrencySymbol + formatCurrency(revenueAmount);
    document.getElementById('filterText1').innerText = revenuePeriod;
}
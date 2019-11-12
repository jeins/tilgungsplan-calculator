$(document).ready(function() {
    // dom variables
    const API_URL = "http://localhost:8080/api";
    const dataFields = {
        startDate: null,
        loan: null,
        interestRate: null,
        durationFixedInterest: null,
        initialRepayment: null,
    };

    const form = $('#formSection');
    const startDate = $('#startDate');
    const loan = $('#loan');
    const interestRate = $('#interestRate');
    const durationFixedInterest = $('#durationFixedInterest');
    const initialRepayment = $('#initialRepayment');
    const submitButton = $('#btnGetRepaymentPlan');

    const resultSection = $('#repaymentPlan');
    const tableBody = $('#tableRepaymentPlan tbody');
    const errorMessage = $('#errorMessage');

    // init setup
    errorMessage.hide();
    resultSection.hide();
    numeralLocaleSetup();
    transformInput();
    startDate.datepicker({ dateFormat: 'dd.mm.yy' });

    // generate repayment plan
    submitButton.click(function() {
        form.removeClass('was-validated');
        resultSection.hide();
        errorMessage.hide();

        //add other field values
        dataFields.startDate = startDate.val();
        dataFields.durationFixedInterest = Number(durationFixedInterest.val());
console.log(dataFields)
        if (isFormValid()) {
            resultSection.show();

            getRepaymentPlan(dataFields, function(data) {
                if (Object.keys(data).length) {
                    renderTableData(data);
                }
            });
        } else {
            form.addClass('was-validated');
        }
    });

// --------------------------------------------------- FUNCTIONS ---------------------------------------------------

    function isFormValid() {
        var isValid = true;

        Object.values(dataFields).forEach((value) => {
            if (!value) {
                isValid = false;
            }
        });

        return isValid;
    }

    function transformInput() {
       loan.focusout(function() {
        dataFields.loan = clearInputNumber(loan.val());
        loan.val(transformValue(clearInputNumber(loan.val()), false));
       });

       interestRate.focusout(function() {
        dataFields.interestRate = clearInputNumber(interestRate.val());
        interestRate.val(transformValue(clearInputNumber(interestRate.val()), false));
       });

       initialRepayment.focusout(function() {
        dataFields.initialRepayment = clearInputNumber(initialRepayment.val());
        initialRepayment.val(transformValue(clearInputNumber(initialRepayment.val()), false));
       });
    }

    function clearInputNumber(num) {
        var newNum = num.replace('.', '').replace(',', '.');
        newNum = Number(newNum);

        return isNaN(newNum) ? 0 : newNum;
    }

    function getRepaymentPlan(requestData, cb) {
       $.ajax({
        url: `${API_URL}/repayment/summary`,
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        success: function(data) {
            cb (data);
        },
        error: function(error) {
            errorMessage.show();
            cb({});
        },
       });
    }

    function renderTableData(data) {
        var html = "";

        $.each(data.monthlyPlans, function(i, plan) {
            html +=
            `<tr>
                <th scope="row">${plan.date}</td>
                <td>${transformValue(plan.remainingDebt)}</td>
                <td>${transformValue(plan.interest)}</td>
                <td>${transformValue(plan.repayment)}</td>
                <td>${transformValue(plan.rate)}</td>
            </tr>`;
        });

         html += renderSummaryData(data);

         tableBody.html(html);
    }

    function renderSummaryData(data) {
        var html =
        `<tr>
           <th scope="row" class="table-info">Zinsbindungsende</td>
           <td class="table-info">${transformValue(data.remainingDebt)}</td>
           <td class="table-info">${transformValue(data.totalInterest)}</td>
           <td class="table-info">${transformValue(data.totalRepayment)}</td>
           <td class="table-info">${transformValue(data.totalRate)}</td>
        </tr>`;

        return html;
    }

    function transformValue(num, withCurrency = true) {
        if (!num && !withCurrency) return null;

        const pattern = withCurrency ? '0,0.00 $' : '0,0.00';

        return numeral(num).format(pattern);
    }

    function numeralLocaleSetup() {
        numeral.register('locale', 'de', {
            delimiters: {
                thousands: '.',
                decimal: ','
            },
            currency: {
                symbol: '€'
            }
        });

        numeral.locale('de');
    }
});

